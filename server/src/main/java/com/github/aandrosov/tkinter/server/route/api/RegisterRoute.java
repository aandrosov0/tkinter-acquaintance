package com.github.aandrosov.tkinter.server.route.api;

import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.entity.CityEntity;
import com.github.aandrosov.tkinter.server.entity.EntityService;
import com.github.aandrosov.tkinter.server.entity.UserEntity;
import com.github.aandrosov.tkinter.toolchain.Validator;

import java.util.HashMap;
import java.util.Map;

public class RegisterRoute implements OnRouteListener {
    private final EntityService entityService;

    public RegisterRoute(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public void listen(Request request, Response response, Map<String, String> ignored) throws Exception {
        if(!request.isFormUrlEncodedContentType()) {
            response.sendHeaders(415, -1);
            return;
        }

        Map<String, String> formUrlEncoded = request.parseFormUrlEncoded();

        String name = formUrlEncoded.get("name");
        String about = formUrlEncoded.get("about");
        String phone = formUrlEncoded.get("phone");
        String password = formUrlEncoded.get("password");

        if((name == null) || (about == null)
                || (phone == null) || (password == null)
                || !formUrlEncoded.containsKey("cityId")) {
            response.sendHeaders(400, -1);
            return;
        }

        long cityId;
        try {
            cityId = Long.parseLong(formUrlEncoded.get("cityId"));
        } catch(NumberFormatException ignoredException) {
            response.sendHeaders(422, -1);
            return;
        }

        if(!Validator.isPhone(phone)  || !Validator.isWord(name)
                || !Validator.isPassword(password)) {
            response.sendHeaders(422, -1);
            return;
        }

        Map<String, Object> comparedCriteria = new HashMap<>();
        comparedCriteria.put("phone", phone);
        if(entityService.isExists(UserEntity.class, comparedCriteria)) {
            response.sendHeaders(409, -1);
            return;
        }

        comparedCriteria.clear();
        comparedCriteria.put("id", cityId);
        if(!entityService.isExists(CityEntity.class, comparedCriteria)) {
            response.sendHeaders(422, -1);
            return;
        }

        UserEntity userEntity = new UserEntity(name, about, phone, password, cityId);
        long id = entityService.insert(userEntity);
        response.sendJson("{\"id\":" + id + "}", 200);
    }

    @Override
    public String getRoute() {
        return "/api/auth/register";
    }
}

package com.github.aandrosov.tkinter.server.route.api;

import com.auth0.jwt.JWT;
import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.Server;
import com.github.aandrosov.tkinter.server.entity.EntityService;
import com.github.aandrosov.tkinter.server.entity.UserEntity;
import com.github.aandrosov.tkinter.toolchain.Hash;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

public class LoginRoute implements OnRouteListener {

    public static final long DEFAULT_EXPIRATION_IN_MINUTES = 30;


    private final EntityService entityService;

    public LoginRoute(EntityService entityService) {
        this.entityService = entityService;
    }

    @Override
    public void listen(Request request, Response response, Map<String, String> routeQuery) throws Exception {
        if(!request.isFormUrlEncodedContentType()) {
            response.sendHeaders(415, -1);
            return;
        }

        Map<String, String> formUrlEncoded = request.parseFormUrlEncoded();
        String phone = formUrlEncoded.get("phone");
        String password = formUrlEncoded.get("password");

        if((phone == null) || (password == null)) {
            response.sendHeaders(400, -1);
            return;
        }

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("phone", phone);

        UserEntity user = (UserEntity) entityService.findOne(UserEntity.class, criteria);
        if(user == null) {
            response.sendHeaders(404, -1);
            return;
        }

        String hashedPassword = Hash.byteArrayToHex(Hash.generateMD5(password.getBytes(StandardCharsets.UTF_8)));
        if(!hashedPassword.equals(user.getPassword())) {
            response.sendHeaders(401, -1);
            return;
        }

        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(DEFAULT_EXPIRATION_IN_MINUTES);

        String token = JWT.create()
                .withExpiresAt(expirationTime.toInstant(ZoneOffset.UTC))
                .withClaim("id", user.getId())
                .sign(Server.SECURITY_ALGORITHM);

        response.sendJson("{\"token\":\"" + token + "\"}", 200);
    }

    @Override
    public String getRoute() {
        return "/api/auth/login";
    }
}

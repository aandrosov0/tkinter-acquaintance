package com.github.aandrosov.tkinter.server.route.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.Server;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class MeUploadPhotoRoute implements OnRouteListener {

    @Override
    public void listen(Request request, Response response, Map<String, String> routeQuery) throws Exception {
        DecodedJWT jwt = request.getJWT();
        if(jwt == null) {
            response.sendHeaders(401, -1);
            return;
        }

        if(!request.isImageContentType()) {
            response.sendHeaders(415, -1);
            return;
        }

        String imageName = jwt.getClaim("id").toString();
        InputStream requestBody = request.getBody();

        Path path = Paths.get(Server.IMAGES_PATH + "/" + imageName);
        java.nio.file.Files.copy(requestBody, path, StandardCopyOption.REPLACE_EXISTING);

        response.sendHeaders(200, -1);
    }

    @Override
    public String getRoute() {
        return "/api/me/upload/photo";
    }
}

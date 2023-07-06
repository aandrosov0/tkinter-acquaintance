package com.github.aandrosov.tkinter.server.route.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.aandrosov.tkinter.server.OnRouteListener;
import com.github.aandrosov.tkinter.server.Request;
import com.github.aandrosov.tkinter.server.Response;
import com.github.aandrosov.tkinter.server.Server;
import com.github.aandrosov.tkinter.toolchain.Streams;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class MeGetPhotoRoute implements OnRouteListener {
    @Override
    public void listen(Request request, Response response, Map<String, String> routeQuery) throws Exception {
        DecodedJWT jwt = request.getJWT();
        if(jwt == null) {
            response.sendHeaders(401, -1);
            return;
        }

        String imageName = jwt.getClaim("id").toString();
        File file = Paths.get(Server.IMAGES_PATH + "/" + imageName).toFile();

        if(!file.exists()) {
            response.sendHeaders(404, -1);
            return;
        }

        try(InputStream inputStream = Files.newInputStream(file.toPath())) {
            byte[] data = Streams.readStream(inputStream);
            response.getHeaders().add("Content-Type", "image/png");
            response.sendHeaders(200, data.length);
            response.getBody().write(data);
        }
    }

    @Override
    public String getRoute() {
        return "/api/me/get/photo";
    }
}

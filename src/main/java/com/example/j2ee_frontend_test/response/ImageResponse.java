package com.example.j2ee_frontend_test.response;

import lombok.Data;

@Data
public class ImageResponse {
    private String status;
    private String message;
    private ImageData data;

    public ImageResponse(String status, String message, ImageData data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getImageUrl() {
        return data.getDisplay_url();
    }


}
class ImageData {
    private String url;

    public String getDisplay_url() {
        return url;
    }
}

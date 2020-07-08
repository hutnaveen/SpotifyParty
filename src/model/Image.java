package model;

import lombok.Builder;
import lombok.Data;

import java.net.URL;
@Data
@Builder
public class Image
{
    int height;
    URL url;
    int width;
}

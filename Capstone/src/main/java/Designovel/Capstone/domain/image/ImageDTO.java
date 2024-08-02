package Designovel.Capstone.domain.image;


import lombok.Data;

@Data
public class ImageDTO {

    private Integer imageId;

    private String url;

    private Integer sequence;

    public ImageDTO(Image image) {
        if (image != null) {
            this.imageId = image.getImageId();
            this.url = image.getUrl();
            this.sequence = image.getSequence();
        }
    }
}

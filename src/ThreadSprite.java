import javafx.application.Platform;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

public class ThreadSprite {
    public Image BlueStarImg;
    public ImageView BlueStarImgView;
    
    public ThreadSprite()
    {
        Image BlueStarImg = new Image("/bluestar10x10.png");
        ImageView BlueStarImgView = new ImageView(BlueStarImg); //sets image to the imageview;
        BlueStarImgView.setFitHeight(BlueStarImg.getHeight());
        BlueStarImgView.setFitWidth(BlueStarImg.getWidth());
    }

    public void SetSpritePosition(double x,double y)
    {
        BlueStarImgView.setX(x);
        BlueStarImgView.setY(y);
    }
    public void RemoveImageView()
    {
        Platform.runLater(
                () -> {
                    
                    App.root.getChildren().remove(BlueStarImgView);
                    }
                );
    }

    public void AddImageView()
    {
        Platform.runLater(
                () -> {
                    
                    App.root.getChildren().add(BlueStarImgView);
                    }
                );
    }

}
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class pentominoes extends Application {

@Override
    
public void start(Stage primaryStage) {
        
    Group root = new Group() ;

    PerspectiveCamera Camera = new PerspectiveCamera(true);
    Camera.setTranslateZ (-5) ;
    Camera.setNearClip (0.2) ;
    Camera.setFarClip (300.0) ;

        
Scene Scene = new Scene(root,500,400,true) ;
Scene.setCamera(Camera);

        

        // penotmino representation for I
        
    Box i1 = CreateSquare (1.5, Color.RED) ; 
            i1.setTranslateX (0) ;
            i1.setTranslateY (0) ;
            i1.setTranslateZ (0) ; 

    Box i2 = CreateSquare(1.5, Color.RED) ;
            i2.setTranslateX (0) ;
            i2.setTranslateY (1) ;
            i2.setTranslateZ (0) ;
 
    Box i3 = CreateSquare(1.5, Color.RED) ;
            i3.setTranslateX (0) ;
            i3.setTranslateY (1) ;
            i3.setTranslateZ (0) ;
    
    Box i4 = CreateSquare(1.5, Color.RED) ;
            i4.setTranslateX (0) ;
            i4.setTranslateY (1) ;
            i4.setTranslateZ (0) ;
 



         // pentomino representation for X   
    
     Box x1 = CreateSquare(1.5, Color.BLACK) ;
            x1.setTranslateX(0);
            x1.setTranslateY(0);
            x1.setTranslateZ(0);

    Box x2 = CreateSquare(1.5, Color.BLACK) ;
            x2.setTranslateX(0);
            x2.setTranslateY(0);
            x2.setTranslateZ(0);
            x2.setRotate(90);

    Box x3 = CreateSquare(1.5, Color.BLACK) ;
            x3.setTranslateX(0);
            x3.setTranslateY(0);
            x3.setTranslateZ(0);
            x2.setRotate(-90);


        // pentomino representation for T

    Box t1 = CreateSquare(1.5, Color.YELLOW);
            t1.setRotate(90);
            t1.setTranslateX(0);
            t1.setTranslateY(0);
           
            
    Box t2 = CreateSquare(1.5, Color.YELLOW);
            t2.setTranslateX(0);
            t2.setTranslateY(1);
            t2.setTranslateZ(0);
            

    Box t3 = CreateSquare(1.5, Color.YELLOW);
            t3.setTranslateX(0);
            t3.setTranslateY(1);
            t3.setTranslateZ(0);
            

            
    
//root.getChildren().addAll(i1,i2,i3,i4) ;      //Show I shape
//root.getChildren().addAll(x1,x2,x3) ;         //Show X shape
//root.getChildren().addAll(t1,t2,t3) ;         //Show T shape



primaryStage.setTitle("3D pentominoes") ;
primaryStage.setScene(Scene) ;
primaryStage.show() ;
    
}

public Box CreateSquare(double size, Color color) {
    
Box box = new Box(0.5, 1.5, 1.5) ;
box.setMaterial(new javafx.scene.paint.PhongMaterial(color)) ;
return box ;

} 

public static void main(String[] args) {
        launch(args) ;
}
}

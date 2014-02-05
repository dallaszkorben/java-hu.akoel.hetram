package hu.akoel.hetram.drawingelements;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import hu.akoel.hetram.gui.MainPanel;
import hu.akoel.mgu.MGraphics;
import hu.akoel.mgu.drawnblock.DrawnBlock;

public class RowPatternBuildingStructuralElement extends DrawnBlock{

	public static enum ORIENTATION{
		HORIZONTAL,
		VERTICAL
	}
	
	private static final long serialVersionUID = -8868671968355924643L;
	
	private final Stroke NORMAL_STROKE = new BasicStroke(1);
	
	private final Color SELECTED_COLOR = Color.red;
	private final Color SELECTED_BACKGROUND = Color.yellow;
	private final Stroke SELECTED_STROKE = new BasicStroke(1);

	//private static final Color INPROCESS_COLOR = Color.yellow;
	//private static final Color INPROCESS_BACKGROUND = Color.gray;
	private final Stroke INPROCESS_STROKE = new BasicStroke(3);
	
	private final Color INFOCUS_COLOR = Color.yellow;
	//private static final Color INFOCUS_BACKGROUND = Color.gray;
	private final Stroke INFOCUS_STROKE = new BasicStroke(1);
	
	private RowPatternInterface rowPatternInterface;
	private MainPanel mainPanel;
	private double lambda;
	private Color color;
	private Color background; 
	
	private RowPatternBuildingStructuralElement(Status status, double x1, double y1,
			java.lang.Double minLength, java.lang.Double maxLength,
			java.lang.Double minWidth, java.lang.Double maxWidth) {
		super(status, x1, y1, minLength, maxLength, minWidth, maxWidth);
	}

	private RowPatternBuildingStructuralElement( Status status, double x1, double y1 ){
		super( status, x1, y1 );
	}
	
	public RowPatternBuildingStructuralElement( RowPatternInterface rowPatternInterface, MainPanel mainPanel, Status status, double x1, double y1, double lambda, Color color, Color background ) {
		super(status, x1, y1);
				
		this.rowPatternInterface = rowPatternInterface;
		this.mainPanel = mainPanel;
		this.lambda = lambda;		
		this.color = color;
		this.background = background;
		
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	public void draw( MGraphics g2 ){

		ORIENTATION orientation; 
		
		int patternWidth;
		int patternHeight;
		
		TexturePaint normalTexturePaint = null;
		TexturePaint selectedTexturePaint = null;
		TexturePaint infocusTexturePaint = null;
		TexturePaint inprocessTexturePaint = null;

		//Szelesebb mint magas
		if( getWidth() > getHeight() ){
			orientation = ORIENTATION.HORIZONTAL;
			patternHeight = mainPanel.getCanvas().getPixelYLengthByWorld(getHeight() ) ;
			patternWidth = (int)(patternHeight / rowPatternInterface.getHeightPerWidth());
			
		//Magasabb mint szeles
		}else{
			orientation = ORIENTATION.VERTICAL;
			patternWidth = mainPanel.getCanvas().getPixelXLengthByWorld(getWidth() );
			patternHeight = (int)(patternWidth / rowPatternInterface.getHeightPerWidth() );
			
		}
	
		//Ha nincs kiterjedese valamelyik iranyba, akkor nem lehet BufferedImage objektumot szerezni
		//De mindegy is, mert akkor nem rajzolunk kitoltest, hiszen csak szel van
		if( patternWidth > 0 && patternHeight > 0 ){		

			//Normal
			BufferedImage bi1 = new BufferedImage( patternWidth, patternHeight, BufferedImage.TYPE_INT_RGB); 
			Graphics2D big1 = bi1.createGraphics();
			big1.setColor( background );
			big1.fillRect( 0, 0, patternWidth, patternHeight );
			big1.setColor( color ); 
		
			// Selected
			BufferedImage bi2 = new BufferedImage( patternWidth, patternHeight, BufferedImage.TYPE_INT_RGB); 
			Graphics2D big2 = bi2.createGraphics();
			big2.setColor( SELECTED_BACKGROUND );
			big2.fillRect( 0, 0, patternWidth, patternHeight );
			big2.setColor( SELECTED_COLOR ); 
		
			// Infocus
			BufferedImage bi3 = new BufferedImage( patternWidth, patternHeight, BufferedImage.TYPE_INT_RGB); 
			Graphics2D big3 = bi3.createGraphics();
			big3.setColor( background );
			big3.fillRect( 0, 0, patternWidth, patternHeight );
			big3.setColor( INFOCUS_COLOR ); 
		
			// Inprocess
			BufferedImage bi4 = new BufferedImage( patternWidth, patternHeight, BufferedImage.TYPE_INT_RGB); 
			Graphics2D big4 = bi4.createGraphics();
			big4.setColor( background );
			big4.fillRect( 0, 0, patternWidth, patternHeight );
			big4.setColor( color ); 
		
			int shift;
			
			//Szelesebb mint magas
			if( orientation.equals( ORIENTATION.HORIZONTAL ) ){

				int pos = mainPanel.getCanvas().getPixelYPositionByWorldBeforeTranslate( getY1() );
				shift = pos - ( (int)(pos / patternHeight ) ) * patternHeight;
			
				if( pos < 0 ){
				
					big1.translate(0, patternHeight);
					big2.translate(0, patternHeight);
					big3.translate(0, patternHeight);
					big4.translate(0, patternHeight);
				}
			
			//Magasabb mint szeles
			}else{
			
				int pos = mainPanel.getCanvas().getPixelXPositionByWorldBeforeTranslate( getX1() );
				shift = pos - ( (int)(pos / patternWidth ) ) * patternWidth;
			
				if( pos < 0 ){
				
					big1.translate( patternWidth, 0 );
					big2.translate( patternWidth, 0 );
					big3.translate( patternWidth, 0 );
					big4.translate( patternWidth, 0 );
				}
		
			}

			//Normal
			big1.setStroke(NORMAL_STROKE);
			rowPatternInterface.drawPattern(big1, orientation, shift, patternWidth, patternHeight);
		
			//Selected
			big2.setStroke(SELECTED_STROKE);
			rowPatternInterface.drawPattern(big2, orientation, shift, patternWidth, patternHeight);
	
			//Infocus
			big3.setStroke(INFOCUS_STROKE);
			rowPatternInterface.drawPattern(big3, orientation, shift, patternWidth, patternHeight);
		
			//Inprocess
			big4.setStroke(INPROCESS_STROKE);
			rowPatternInterface.drawPattern(big4, orientation, shift, patternWidth, patternHeight);
			
			Rectangle r = new Rectangle( 0, 0, patternWidth, patternHeight );
			normalTexturePaint = new TexturePaint( bi1,r );
			selectedTexturePaint = new TexturePaint( bi2,r ); 
			infocusTexturePaint = new TexturePaint( bi3,r );
			inprocessTexturePaint = new TexturePaint( bi4,r );
		}
		
		setNormal( color, NORMAL_STROKE, normalTexturePaint );
		setSelected( SELECTED_COLOR, SELECTED_STROKE, selectedTexturePaint );
		setInfocus(INFOCUS_COLOR, INFOCUS_STROKE, infocusTexturePaint );
		setInprocess( color, INPROCESS_STROKE, inprocessTexturePaint );
		
		super.draw(g2);

	}			
	
}












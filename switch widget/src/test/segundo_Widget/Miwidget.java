package test.segundo_Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;


/*Creado por Sebastian Cipolat Buenos Aires Argentina 2012*/ 

public class Miwidget extends AppWidgetProvider{
	
    
    private static final String ACTION_cambiarlayout = "a_cambiarlayout";  

    int status=1;
    public String shprefreg="MSG_switch_status"; 
	
	 @Override
	    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	    int[] appWidgetIds) {

		 /*Accedemos al shared preference  shprefreg  y tratamos de leer, si hay algun error
		   mensaje tomara el valor "error" lo que indicara que no existe dentro de los shared preference
		   'MSG_switch_status' con lo cual lo creamos y añadimos  como valor off*/
		   
		 SharedPreferences prefs =context.getSharedPreferences(shprefreg, Context.MODE_PRIVATE);
		 String mensaje = prefs.getString(shprefreg, "error");
       
		 if (mensaje=="error"){ 
			 
		//si cuando intenta leer es error añade un registro.
         SharedPreferences.Editor editor = prefs.edit();
         editor.putString(shprefreg, "off"); //Estado apagado
         editor.commit();
         
         Log.e(" SharedPreferences error read", ""+mensaje);
         Log.e(" SharedPreferences W ->", "off");
         mensaje="off";

         }else{
        	 //No hubo error, ya existia .
             Log.e(" SharedPreferences read ok", ""+mensaje);


         }
		 //Actualizamos el widget con el estado leido previamente
    	 actualizarWidget(context, appWidgetManager, mensaje); 

		 
		   }
		 		 
	 public static void actualizarWidget(Context context,AppWidgetManager appWidgetManager, String value)
	{	
		 
	   RemoteViews remoteViews ; 

       ComponentName thisWidget;
       
       int lay_id=0;
      
       //Asignamos el layout a la variable lay_id segun el parametro recibido por value
       if (value.equals("on")){
    	   //ON
    	    lay_id= R.layout.main_on;
    	          }
      
       if (value.equals("off")){
    	   //off
    	    lay_id= R.layout.main_off;

       }
          //Vamos a acceder a la vista y cambiar el layout segun lay_id       
          remoteViews = new RemoteViews(context.getPackageName(), lay_id);
          //identifica a nuestro widget
		  thisWidget = new ComponentName(context, Miwidget.class);
		 
		   //Creamos un intent a nuestra propia clase
		   Intent intent = new Intent(context, Miwidget.class);
		   //seleccionamos la accion ACTION_cambiarlayout
           intent.setAction(ACTION_cambiarlayout); 
		    	  
           
           PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
           
           /*Equivalente a setOnClickListener de un boton comun lo asocio con 
           el layout1 ya que al tocar este se ejecutara la accion 
           y con pendingIntent */
           
           remoteViews.setOnClickPendingIntent(R.id.layout1, pendingIntent);
        
           //actualizamos el widget
           appWidgetManager.updateAppWidget(thisWidget, remoteViews); 
	}
		 
	 
	 @Override
     public void onReceive(Context context, Intent intent) {
		 //Controlamos que la accion recibida sea la nuestra
         if (intent.getAction().equals(ACTION_cambiarlayout)) {
         //Leemos nuevamente SharedPreferences
		 SharedPreferences prefs = context.getSharedPreferences(shprefreg, Context.MODE_PRIVATE);
		 String mensaje = prefs.getString(shprefreg, "error");
		 SharedPreferences.Editor editor = prefs.edit();
		 
		 String new_status="";
		 
         Log.e("! :)  status onReceive! ", mensaje);
         
         /* Si el estado que leimos es on definimos que el nuevo sea off y lo grabamos en  SharedPreferences
          * realizamos lo mismo con off pero usando on
          * El valor grabado lo utilizaremos para determinar el layout a cargar*/
         
         if (mensaje.equals("on")){
        	 
        	 editor.putString(shprefreg , "off");
             editor.commit();
             Log.e("! :)  shprefreg write -> ", "off");
             new_status= "off";

         }
         else if (mensaje.equals("off")){
        	 editor.putString(shprefreg , "on");
             editor.commit();
             Log.e("! :)  shprefreg write -> ", "on");
             new_status= "on";

        	 }
                  
		 
            //Actualizamos el estado del widget.
             AppWidgetManager widgetManager =AppWidgetManager.getInstance(context);
            
             actualizarWidget( context,widgetManager ,new_status);

             
         }
                	
          super.onReceive(context, intent);
     }

	
	
	
	 }


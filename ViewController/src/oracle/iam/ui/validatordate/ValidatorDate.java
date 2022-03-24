package oracle.iam.ui.validatordate;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Descripcion: Valida que la fecha final no sea mayor a 365 dias
 * y no se encuentre antes de la fecha de inicio.
 */
public class ValidatorDate {
    
    /**
     * Representa la constante del componente de fecha final.
     */
    private UIComponent endDateID;
    /**
     * Representa la constante del componente de fecha inicial.
     */
    private UIComponent startDateID;
    
    
    //Getters y setters de los componentes fecha inicial y fecha final.
     public void setStartDateID(UIComponent startDateID) {
         this.startDateID = startDateID;
     }
    public UIComponent getStartDateID() {
         return startDateID;
     }
     public void setEndDateID(UIComponent endDateID) {
         this.endDateID = endDateID;
     }
     public UIComponent getEndDateID() {
         return endDateID;
     }
     
     /**
      * Representa el mensaje que saldra si la fecha final es mayor a 365 dias.
      */
    private static final String START_DATE_END_DATE_VALIDATION_MSG =
        "El intervalo entre Fecha de inicio y fecha final no puede ser mayor a 365 dias.";
    /**
     * Representa el mensaje que saldra si la fecha final es menor a la fecha inicial.
     */
    private static final String START_DATE_AFTER_END_DATE_VALIDATION_MSG = "La fecha de inicio no puede ser posterior a la fecha final";
    /**
     * Representa el nombre del atributo de la fecha inicial.
     */
    private static final String START_DATE_ATTRIBUTE = "usr_start_date__c";
    
    /**
     * Se encarga de traer cada uno de los componentes que se necesitan para hacer la validaci?n.
     * @param facesContext : se encarga del procesamiento de una solicitud y crea la respuesta que solicitemos.
     * @param uiComponent :componente que contiene la funci?n.
     */
    public void validator(FacesContext facesContext, UIComponent uiComponent, Object object) {
             //si el componente que le pasamos es igual al de fecha final ejecuta la funci?n.
             if (uiComponent.equals(endDateID)) {
                //utilizando la clase FacesUtils trae la fecha que tenga el nombre del atributo que le damos.
               oracle.jbo.domain.Date jboStartDate = FacesUtils.getAttributeBindingValue(START_DATE_ATTRIBUTE, oracle.jbo.domain.Date.class);
                //ejecuta la validaci?n solo si ambos datos ya est?n colocados.        
               if(jboStartDate != null){
                   try{
                       //Trae el valor de la fecha inicial.
                       Date startDate = jboStartDate.getValue();
                       //Trae el valor de la fecha final.
                       Date endDate = ((oracle.jbo.domain.Date)object).getValue(); 
                       //Ejecuta la validaci?n.
                       validateStartDateEndDate(facesContext, uiComponent, startDate, endDate);
                   }catch(NullPointerException e){
                       //se ejecuta si el valor de la fecha que contiene el validador es nulo.
                       System.out.print("Fecha final con valor nulo.");
                   }
               }
             }
     }
     
    /**
     * Encargado de que la fecha final no sea mayor a un a?o y no se encuentre antes de la fecha de inicio.
     * @param startDate atributo de fecha inicial.
     * @param endDate atributo de fecha final.
     */
    private void validateStartDateEndDate(FacesContext facesContext, UIComponent uiComponent, Date startDate, Date endDate) {
        //Variable encargada de calcular el valor de la fecha de un a?o despues a partir de la fecha inicial. 
        Date startDatePlus365Days = new Date(startDate.getTime() + 365L * 24 * 60 * 60 * 1000);
        //Si la fecha inicial est? despu?s de la fecha final muestra mensaje de error.
        if (startDate.after(endDate)) {
            facesContext.addMessage(uiComponent.getClientId(facesContext),
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,START_DATE_AFTER_END_DATE_VALIDATION_MSG));
        //Si la fecha final es mayor a un a?o desde la fecha inicial muestra mensaje de error.
        } else if (startDatePlus365Days.before(endDate)) {
            facesContext.addMessage(uiComponent.getClientId(facesContext),
                                    new FacesMessage(FacesMessage.SEVERITY_ERROR, null,START_DATE_END_DATE_VALIDATION_MSG));
        }
        //Si no hay error, no se ejecuta ningun cambio.
        else{
            /*clase FacesUtils:
            * https://docs.oracle.com/en/middleware/idm/identity-governance/12.2.1.4/omdev/facesutils-class.html#GUID-F6C3EC78-05C7-4583-9AFF-A59B38F7F240
            */
            FacesUtils.partialRender(startDateID);
            FacesUtils.partialRender(endDateID);
        }
    }
}

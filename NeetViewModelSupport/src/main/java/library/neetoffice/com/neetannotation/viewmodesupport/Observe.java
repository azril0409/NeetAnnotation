package library.neetoffice.com.neetannotation.viewmodesupport;

/**
 * Created by azril on 2018/2/21.
 */

public @interface Observe {
    String viewmodelName ();
    String subscriber () default  "";
}

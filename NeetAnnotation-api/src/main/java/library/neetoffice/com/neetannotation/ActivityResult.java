package library.neetoffice.com.neetannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Methods annotated with @{@link ActivityResult} will be called on onActivityResult
 * <pre>
 * &#064;NActivity()
 * public class MyActivity extends Activity {
 *  &#064;ActivityResult()
 *  void onResult(Bundle bundle) {
 *  }
 * }
 * </pre>
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface ActivityResult {
    /**
     * Use ActivityResult.Contract
     *
     * @return ActivityResult.Contract, default StartActivityForResult.
     */
    Contract value() default ActivityResult.Contract.StartActivityForResult;

    /**
     * The parameter will be efficient when value be StartActivityForResult and StartIntentSenderForResult.
     *
     * @return result code.
     */
    int resultCode() default -1;

    /**
     * The annotation value is the key used for extra. If not set, the field or method name will be used as the key.
     * */
    @Retention(SOURCE)
    @Target({PARAMETER})
    @interface Extra {
        /**
         * Bundle key
         *
         * @return key
         */
        String value() default "";
    }

    /**
     * Reference： https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContract
     */
    enum Contract {
        /**
         * An ActivityResultContract to take a video saving it into the provided content-Uri.
         */
        CaptureVideo,
        /**
         * An ActivityResultContract to prompt the user to select a path for creating a new document, returning the content:Uri of the item that was created.
         */
        CreateDocument,
        /**
         * An ActivityResultContract to prompt the user to pick a piece of content, receiving a content://Uri for that content that allows you to use android.content.ContentResolver.openInputStream to access the raw data.
         */
        GetContent,
        /**
         * An ActivityResultContract to prompt the user to pick one or more a pieces of content, receiving a content://Uri for each piece of content that allows you to use android.content.ContentResolver.openInputStream to access the raw data.
         */
        GetMultipleContents,
        /**
         * An ActivityResultContract to prompt the user to select a directory, returning the user selection as a Uri.
         */
        OpenDocumentTree,
        /**
         * An ActivityResultContract to prompt the user to open a document, receiving its contents as a file:/http:/content:Uri.
         */
        OpenDocument,
        /**
         * An ActivityResultContract to prompt the user to open (possibly multiple) documents, receiving their contents as file:/http:/content:Uris.
         */
        OpenMultipleDocuments,
        /**
         * An ActivityResultContract to request the user to pick a contact from the contacts app.
         */
        PickContact,
        /**
         * An ActivityResultContract to request permissions
         */
        RequestMultiplePermissions,
        /**
         * An ActivityResultContract to request a permission
         */
        RequestPermission,
        /**
         * An ActivityResultContract that doesn't do any type conversion, taking raw Intent as an input and ActivityResult as an output.
         */
        StartActivityForResult,
        /**
         * An ActivityResultContract that calls Activity.startIntentSender.
         */
        StartIntentSenderForResult,
        /**
         * An ActivityResultContract to take small a picture preview, returning it as a Bitmap.
         */
        TakePicturePreview,
        /**
         * An ActivityResultContract to take a picture saving it into the provided content-Uri.
         */
        TakePicture,
    }
}

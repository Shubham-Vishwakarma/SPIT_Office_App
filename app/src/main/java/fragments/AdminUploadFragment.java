package fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.spit.AdminActivity;
import com.example.admin.spit.R;
import com.example.admin.spit.Topics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminUploadFragment extends Fragment {

    Button choose_button,upload_button;
    EditText title_edittext,description_editext;
    TextView selected_file;
    Spinner category_spinner;
    String category,title,description,path_URI=null;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    String extension;

    static final int READ_REQUEST_CODE=100;

    public AdminUploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_upload, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        choose_button=(Button)view.findViewById(R.id.choose_button);
        upload_button=(Button)view.findViewById(R.id.upload_button);
        category_spinner=(Spinner) view.findViewById(R.id.category_list_id);
        title_edittext=(EditText)view.findViewById(R.id.title_edittext);
        description_editext=(EditText)view.findViewById(R.id.description_edittext);
        selected_file=(TextView)view.findViewById(R.id.selected_file);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        choose_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent,READ_REQUEST_CODE);
            }
        });

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                category=category_spinner.getSelectedItem().toString();
                title=title_edittext.getText().toString();
                description=description_editext.getText().toString();

                if(isValid())
                {
                    showProgressDialog();
                    storageReference= FirebaseStorage.getInstance().getReference().child(category).child(title+"."+extension);
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("Topics").child(category+" Topics");
                    uploadFile(category,title,description,extension);
                }
                else
                {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    void uploadFile(String category, final String title, final String description,final String extension)
    {
        try
        {
            Uri file=Uri.parse(path_URI);
            storageReference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(),"Success",Toast.LENGTH_SHORT).show();
                    Topics topics=new Topics(title+"."+extension,description);
                    databaseReference.push().setValue(topics);
                    hideProgressDialog();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),"Failure",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver contentResolver= getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                selected_file.setText(data.getDataString());
                path_URI=uri.toString();
                extension=mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
            }
        }
    }

    boolean isValid()
    {
        if(title.isEmpty())
        {
            title_edittext.setError("Set Title");
            title_edittext.setFocusable(true);
            return false;
        }

        if(description.isEmpty())
        {
            description_editext.setError("Set Description");
            description_editext.setFocusable(true);
            return false;
        }

        return true;
    }

    public void showProgressDialog()
    {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading file...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        progressDialog.dismiss();
    }
}
package fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.spit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends Fragment {

    static final String TAG="FORGOTPASSWORDFRAGMENT";

    EditText forgotpassword_edittext;
    Button forgotpassword_button;
    String email;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forgotpassword_edittext=(EditText)view.findViewById(R.id.forgotpassword_edittext);
        forgotpassword_button=(Button)view.findViewById(R.id.forgotpassword_button);

        forgotpassword_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=forgotpassword_edittext.getText().toString();

                if(email.isEmpty())
                {
                    forgotpassword_edittext.setError("Email");
                }
                else
                {
                    showProgressDialog();
                    firebaseAuth=FirebaseAuth.getInstance();
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        hideProgressDialog();
                                        Toast.makeText(getActivity(),"Email sent", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        hideProgressDialog();
                                        Toast.makeText(getActivity(),"Error Occured", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

    }

    public void showProgressDialog()
    {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Sending Email...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        progressDialog.dismiss();
    }
}

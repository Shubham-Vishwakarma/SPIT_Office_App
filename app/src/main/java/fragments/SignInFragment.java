package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.spit.R;
import com.example.admin.spit.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.admin.spit.R.id.CPassword;
import static com.example.admin.spit.R.id.gender_radiogroup;
import static com.example.admin.spit.R.id.male_radiobutton;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private Button done;
    private EditText name,email,password,cpassword,uid;
    private RadioGroup gender_radiogroup;
    private RadioButton gender_radiobutton_male,getGender_radiobutton_female;
    Spinner department_spinner;
    private String Name,Email,Password,CPassowrd,Uid,Gender,Department;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    public static final String TAG="SIGNIN_ACTIVITY";
    public static final String ADMIN_EMAIL_ID="@spit.ac.in";
    public static final String ADMIN_DEPARTMENT="Office/Faculty";
    String userId;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        done=(Button)view.findViewById(R.id.done_button);
        name=(EditText)view.findViewById(R.id.Name);
        uid=(EditText)view.findViewById(R.id.uid);
        gender_radiogroup=(RadioGroup)view.findViewById(R.id.gender_radiogroup);
        gender_radiobutton_male=(RadioButton)view.findViewById(R.id.male_radiobutton);
        getGender_radiobutton_female=(RadioButton)view.findViewById(R.id.female_radiobutton);
        department_spinner=(Spinner)view.findViewById(R.id.department_spinner);
        email=(EditText)view.findViewById(R.id.Email);
        password=(EditText)view.findViewById(R.id.Password);
        cpassword=(EditText)view.findViewById(CPassword);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(gender_radiobutton_male.isChecked())
                {
                    Gender=gender_radiobutton_male.getText().toString();
                }
                else if(getGender_radiobutton_female.isChecked())
                {
                    Gender=getGender_radiobutton_female.getText().toString();
                }

                Department=department_spinner.getSelectedItem().toString();

                Name=name.getText().toString();
                Email=email.getText().toString();
                Password=password.getText().toString();
                CPassowrd=cpassword.getText().toString();
                Uid=uid.getText().toString();

                if(!isValidate())
                {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    showProgressDialog();
                    createAccount();
                }

            }
        });

    }

    public void createAccount()
    {
        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                            //sendVerificationEmail();
                            uploadData();
                        }
                        else if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Registration failed.", Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }
                    }
                });
    }

    public void sendVerificationEmail()
    {
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            hideProgressDialog();
                            Toast.makeText(getActivity(),"Email verification sent to your mail",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean isValidate()
    {
        if(Name.isEmpty())
        {
            name.setError("Name Required");
            name.setFocusable(true);
            return false;
        }

        if(Uid.isEmpty())
        {
            uid.setError("UID Required");
            uid.setFocusable(true);
            return false;
        }

        if (Email.isEmpty())
        {
            email.setError("Email Required");
            email.setFocusable(true);
            return false;
        }

        if (Password.isEmpty())
        {
            password.setError("Password Required");
            password.setFocusable(true);
            return false;
        }

        if (!(CPassowrd.equals(Password)))
        {
            cpassword.setError("Password should be same");
            cpassword.setFocusable(true);
            return false;
        }
        if(!Department.equals(ADMIN_DEPARTMENT)  &&  Email.contains(ADMIN_EMAIL_ID))
        {
            email.setError("Invalid Email");
            email.setFocusable(true);
            return false;
        }
        if(Department.equals(ADMIN_DEPARTMENT)  &&  !Email.contains(ADMIN_EMAIL_ID))
        {
            email.setError("Invalid Email");
            email.setFocusable(true);
            return false;
        }

        return true;
    }

    public void showProgressDialog()
    {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registering to SPIT...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        progressDialog.dismiss();
    }

    public void uploadData()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null)             // User is signed in
        {
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            userId=user.getUid();
            writeNewUser(userId,Name,Email,Uid,Gender,Department);
        }
        else                          // User is signed out
        {
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
        // ...
    }

    public void writeNewUser(String userId,String Name,String Email,String UID,String Gender,String Department)
    {
        User user = new User(Name,Email,UID,Gender,Department);
        databaseReference.child("users").child(userId).setValue(user);
    }
}
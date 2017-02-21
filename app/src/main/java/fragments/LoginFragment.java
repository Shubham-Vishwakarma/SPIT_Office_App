package fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.spit.AdminActivity;
import com.example.admin.spit.R;
import com.example.admin.spit.StudentActivity;
import com.example.admin.spit.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText email,password;
    private String Email,passWord;
    private String username,department;
    private Button login,signIn,forgotPassword;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    public static final String TAG="LOGIN_FRAGMENT";
    public static final String ADMIN_EMAIL_SPIT="@spit.ac.in";
    public static final String ADMIN_DEPARTMENT="Office/Faculty";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email=(EditText)view.findViewById(R.id.email);
        password=(EditText)view.findViewById(R.id.password);
        login=(Button)view.findViewById(R.id.login_button);
        signIn=(Button)view.findViewById(R.id.signIn_button);
        forgotPassword=(Button)view.findViewById(R.id.forgotpassword_button);

        firebaseAuth=FirebaseAuth.getInstance();

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)                // User is signed in
                {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    String email=user.getEmail();

                    if (email.contains(ADMIN_EMAIL_SPIT)) {
                        startActivity(new Intent(getActivity(),AdminActivity.class));
                    }
                    else
                    {
                        startActivity(new Intent(getActivity(),StudentActivity.class));
                    }
                } else
                {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email=email.getText().toString();
                passWord=password.getText().toString();

                if(Email.isEmpty())
                {
                    email.setError("Enter Username");
                }
                if(passWord.isEmpty())
                {
                    password.setError("Enter Password");
                }
                else
                {
                    showProgressDialog();
                    signInAccount();
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                SignInFragment signInFragment=new SignInFragment();
                fragmentTransaction.replace(R.id.login_fragment_container,signInFragment);
                fragmentTransaction.commit();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                ForgotPasswordFragment forgotPasswordFragment=new ForgotPasswordFragment();
                fragmentTransaction.replace(R.id.login_fragment_container,forgotPasswordFragment);
                fragmentTransaction.commit();
            }
        });
    }

    public void signInAccount()
    {
        firebaseAuth.signInWithEmailAndPassword(Email, passWord)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
//                            if(firebaseUser.isEmailVerified())
//                            {
                                databaseReference= FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
                                retrieveData();
                                hideProgressDialog();
                                if(Email.contains(ADMIN_EMAIL_SPIT))
                                {
                                    Intent intent=new Intent(getActivity(), AdminActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent=new Intent(getActivity(),StudentActivity.class);
                                    startActivity(intent);
                                }
//                            }
//                            else
//                            {
//                                hideProgressDialog();
//                                Toast.makeText(getActivity(), "Invalid User",Toast.LENGTH_SHORT).show();
//                            }
                        }
                        else if (!task.isSuccessful())
                        {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            hideProgressDialog();
                            Toast.makeText(getActivity(), "Invalid User",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showProgressDialog()
    {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logining to SPIT...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        progressDialog.dismiss();
    }

    public void retrieveData()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                User user=dataSnapshot.getValue(User.class);
                username=user.Name;
                department=user.Department;
                Toast.makeText(getActivity(),username,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
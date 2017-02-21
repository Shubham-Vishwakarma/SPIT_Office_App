package fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.spit.R;
import com.example.admin.spit.User;
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
public class CertificateFragment extends Fragment {

    public CertificateFragment() {}

    TextView username_textview;
    Button apply;
    EditText reason;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    String reasonForBonafide;
    String username;
    String UID;
    String Email;
    String Gender;
    String Department;
    String TO[];
    final String SUBJECT = "Request for new Bonafide Certificate";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_certificate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TO=getResources().getStringArray(R.array.authority_emails);
        retrieveData();

        username_textview=(TextView)view.findViewById(R.id.Name);
        apply=(Button)view.findViewById(R.id.apply_bonafide);
        reason=(EditText)view.findViewById(R.id.reason_bonafide);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reasonForBonafide=reason.getText().toString();

                if(reasonForBonafide.isEmpty())
                {
                    reason.setError("Enter Reason");
                    reason.setFocusable(true);
                }
                else {
                    sendEmail();
                }

            }
        });
    }

    void sendEmail()
    {
        String MESSAGE = username + "\n" + UID + "\n" + Gender + "\n" + Department + "\n" + reasonForBonafide;
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, TO);
            intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
            intent.putExtra(Intent.EXTRA_TEXT, MESSAGE);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            Toast.makeText(getActivity(),"No Email App Present",Toast.LENGTH_SHORT).show();
        }
    }

    void retrieveData()
    {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username_textview.setText(user.Name);
                username=user.Name;
                UID=user.UID;
                Email=user.Email;
                Gender=user.Gender;
                Department=user.Department;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
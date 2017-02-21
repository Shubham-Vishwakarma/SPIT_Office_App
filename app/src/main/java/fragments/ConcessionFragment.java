package fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConcessionFragment extends Fragment {

    TextView username_textview;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String username,UID,Email,Gender,Department;

    Spinner line_spinner;
    Spinner from_station;
    /*Spinner*/ TextView to_station;

    String from_station_string, to_station_string, line;
    ArrayAdapter<String> from_station_list;
    //ArrayAdapter<String> to_station_list;
    Button apply_concession;
    Button scholarship_button;

    String TO[];
    final String SUBJECT="Railway Concession";
    final String LINE="Line:  ";
    final String FROM_STATION="From Station:  ";
    final String TO_STATION="To Station:  ";

    public ConcessionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_concession, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrieveData();
        TO= getResources().getStringArray(R.array.authority_emails);

        username_textview=(TextView)view.findViewById(R.id.Name);
        line_spinner=(Spinner)view.findViewById(R.id.lines_spinner);
        from_station=(Spinner)view.findViewById(R.id.from_station_id);
        to_station=(TextView)view.findViewById(R.id.to_station_id);
        apply_concession=(Button)view.findViewById(R.id.apply_concession_button);
        scholarship_button=(Button)view.findViewById(R.id.scholarship_button);

        from_station_list=setCentral();
        //to_station_list=setCentral();
        from_station.setAdapter(from_station_list);
        //to_station.setAdapter(to_station_list);

        line_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                line=adapterView.getSelectedItem().toString();
                switch(line)
                {
                    case "Central Railway":from_station_list=setCentral();
          //                 to_station_list=setCentral();
                         break;
                    case "Western Railway":from_station_list=setWestern();
            //               to_station_list=setWestern();
                        break;
                    case "Harbour Railway":from_station_list=setHarbour();
              //             to_station_list=setHarbour();
                        break;
                }
                from_station.setAdapter(from_station_list);
                //to_station.setAdapter(to_station_list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        from_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from_station_string=adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
/*
        to_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to_station_string=adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/
        apply_concession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });

        scholarship_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.spit.ac.in/academics/scholarships/"));
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getActivity(),"No WebBrower application present",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void sendEmail()
    {
        String MESSAGE = username + "\n" + UID + "\n" + Gender + "\n" + Department + "\n" +
                LINE+line+"\n"+FROM_STATION+from_station_string+"\n"+TO_STATION+to_station_string+"\n";
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

    ArrayAdapter<String> setCentral()
    {
        String list[]=getResources().getStringArray(R.array.central_station_names);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        return adapter;
    }

    ArrayAdapter<String> setWestern()
    {
        String list[]=getResources().getStringArray(R.array.western_station_names);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        return adapter;
    }

    ArrayAdapter<String> setHarbour()
    {
        String list[]=getResources().getStringArray(R.array.harbour_station_names);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        return adapter;
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

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
import android.widget.Toast;

import com.example.admin.spit.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutAppFragment extends Fragment {

    Button submit_feedback_button;
    EditText submit_feedback_text;
    String feedback;
    String TO[];
    final String SUBJECT="Feedback";

    public AboutAppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TO=getResources().getStringArray(R.array.authority_emails);

        submit_feedback_button=(Button)view.findViewById(R.id.submit_feedback_button);
        submit_feedback_text=(EditText)view.findViewById(R.id.submit_feedback_text);


        submit_feedback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                feedback = submit_feedback_text.getText().toString();
                if(feedback.isEmpty())
                {
                    submit_feedback_text.setError("Enter feedBack");
                }
                else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL, TO);
                        intent.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                        intent.putExtra(Intent.EXTRA_TEXT, feedback);
                        startActivity(intent);
                    }
                    catch (ActivityNotFoundException e)
                    {
                        Toast.makeText(getActivity(),"No Email App Present",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}

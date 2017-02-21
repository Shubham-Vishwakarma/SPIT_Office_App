package fragments;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.spit.CustomListView;
import com.example.admin.spit.R;
import com.example.admin.spit.Topics;
import com.example.admin.spit.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ACCESSIBILITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExaminationFragment extends Fragment {

    TextView username_textview;
    ListView listView;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    ArrayList<String> examinationTopicsList=new ArrayList<String>();
    ArrayList<String> examinationDescriptionList=new ArrayList<String>();
    CustomListView customListView;

    public ExaminationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_examination, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrieveData();
        username_textview=(TextView)view.findViewById(R.id.Name);
        listView=(ListView)view.findViewById(R.id.examination_listview);
        customListView=new CustomListView(getActivity(),examinationTopicsList);
        listView.setAdapter(customListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                downloadFileViaNotification(examinationTopicsList.get(i),i);
            }
        });

        updateExaminationListView();
    }

    public void updateExaminationListView()
    {
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Topics").child("Examination Topics");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Topics topics=dataSnapshot.getValue(Topics.class);
                String temp_title=topics.title;
                customListView.add(temp_title);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Topics topics=dataSnapshot.getValue(Topics.class);
                String temp_title=topics.title;
                customListView.add(temp_title);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void downloadFileViaNotification(final String FileName, final int id) {
        Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();
        storageReference = FirebaseStorage.getInstance().getReference().child("Examination").child(FileName);

        final File filePath = new File(Environment.getExternalStorageDirectory() + "/SPIT");
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File localFile = new File(filePath, FileName);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getActivity());
        builder.setSmallIcon(R.drawable.download_icon1).setContentTitle(FileName).setContentText("Downloading...");

        storageReference.getFile(localFile).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                double progress = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                builder.setProgress(100, (int) progress, false);
                notificationManager.notify(id, builder.build());
            }
        }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                builder.setContentText("Download Complete").setProgress(0, 0, false);
                notificationManager.notify(id, builder.build());

                try {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setData(Uri.parse(filePath + "/" + FileName));
                    intent.setType("*/*");

                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    notificationManager.notify(id, builder.build());
                } catch (Exception e) {

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                builder.setContentText("Download Failed").setProgress(0, 0, false);
                notificationManager.notify(id, builder.build());
            }
        });
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
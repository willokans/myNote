package androidproject.com.mynotes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText rFName;
    private EditText rEmail;
    private EditText rPasd;
    private EditText rRetypePasd;
    private Button rBtn;
    private TextView textVSignin;

    //use a progress bar to show progress in registration
    private ProgressDialog progressDialog;

    //Firebase Objects
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Set reerence to mAuth
        mAuth = FirebaseAuth.getInstance();

        //initilize progressbar
        progressDialog = new ProgressDialog(this);


        rFName = (EditText) findViewById(R.id.regFName);
        rEmail = (EditText) findViewById(R.id.regemailField);
        rPasd = (EditText) findViewById(R.id.regPasswordField);
        rRetypePasd = (EditText) findViewById(R.id.regRetypePasswordField);
        rBtn = (Button) findViewById(R.id.regBtn);
        textVSignin = (TextView) findViewById(R.id.textViewSignin);

        //add listener to rBtn button
        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    registerUser();

                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });

        textVSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }

    //create a register user method
    private void registerUser() {

        //retrive email and password entered by user
        String email = rEmail.getText().toString().trim();
        String password = rPasd.getText().toString().trim();

        //check of Edit box is empty
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Please enter email address!", Toast.LENGTH_LONG).show();
            //return will stop the function from executing further
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Please enter password!", Toast.LENGTH_LONG).show();
            //return will stop the function from executing further
            return;
        }

        if (TextUtils.isEmpty(email) && (TextUtils.isEmpty(password))) {
            Toast.makeText(RegisterActivity.this, "Please enter email address and Password!", Toast.LENGTH_LONG).show();
            //return will stop the function from executing further
            return;
        }


        //show a progress Dialog bar
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        //initialize firebase Authentication to create a user
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //execute when registration is completed
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(RegisterActivity.this, "Could not Register.. Please tyr again.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }





}

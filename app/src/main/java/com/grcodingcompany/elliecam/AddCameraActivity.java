package com.grcodingcompany.elliecam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddCameraActivity extends AppCompatActivity {

    EditText cameraNameInput;
    EditText urlInput;
    EditText usernameInput;
    EditText passwordInput;

    Button saveCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera);

        Spinner spinner = (Spinner) findViewById(R.id.cameraType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            this,
            R.array.camera_types, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setTitle(R.string.add_camera_title);

        cameraNameInput = (EditText) findViewById(R.id.input_camera_name);
        urlInput = (EditText) findViewById(R.id.input_url);
        usernameInput = (EditText) findViewById(R.id.input_username);
        passwordInput = (EditText) findViewById(R.id.input_password);

        urlInput.addTextChangedListener(new MyTextWatcher());
        usernameInput.addTextChangedListener(new MyTextWatcher());
        passwordInput.addTextChangedListener(new MyTextWatcher());

        saveCameraButton = (Button) findViewById(R.id.btn_save);

        saveCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCamera();
            }
        });
    }

    private void checkRequiredFields()
    {
        String cameraUrl = urlInput.getText().toString();

        boolean hasValues = !cameraNameInput.getText().toString().trim().isEmpty() &&
                !cameraUrl.trim().isEmpty() &&
                !usernameInput.getText().toString().trim().isEmpty() &&
                !passwordInput.getText().toString().trim().isEmpty();

        if (!Patterns.WEB_URL.matcher(cameraUrl).matches()) {
            hasValues = false;
        }

        saveCameraButton.setEnabled(hasValues);
    }

    private void saveCamera()
    {
        Camera newCamera = new Camera();
        newCamera.name = cameraNameInput.getText().toString().trim();
        newCamera.url = urlInput.getText().toString().trim();
        newCamera.username = usernameInput.getText().toString().trim();
        newCamera.password = passwordInput.getText().toString().trim();

        newCamera.save();

        setResult(RESULT_OK);
        finishFromChild(this);
    }

    private class MyTextWatcher implements TextWatcher {

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            checkRequiredFields();
        }
    }
}

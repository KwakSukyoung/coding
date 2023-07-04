package com.example.familymapclient;

import Model.Person;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.RegisterResult;

public class LoginFragment extends Fragment {
    //default functions
    public LoginFragment() {}
    public void LoginListener(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //private variables
    private static final String FINAL_RESULT = "TotalResult";
    String Gender = "";
    private static Person person;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Server Host
        EditText serverhost = view.findViewById(R.id.ServerHost);
        //Server Post
        EditText serverport = view.findViewById(R.id.ServerPort);
        //User Name
        EditText username = view.findViewById(R.id.UserName);
        //Password
        EditText password = view.findViewById(R.id.Password);
        //First Name
        EditText firstname = view.findViewById(R.id.FirstName);
        //Last Name
        EditText lastname = view.findViewById(R.id.LastName);
        //Email
        EditText email = view.findViewById(R.id.Email);
        //Gender
        RadioGroup radioGroup = view.findViewById(R.id.Gender);
        //Radio buttons
        Button buttonL = view.findViewById(R.id.SIGNIN);
        Button buttonR = view.findViewById(R.id.REGISTER);

        // Enabled button
        TextWatcher textWatcher = new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonL.setEnabled((!username.getText().toString().isEmpty())&&
                        (!password.getText().toString().isEmpty())&&
                        (!serverhost.getText().toString().isEmpty())&&
                        (!serverport.getText().toString().isEmpty()));
                buttonR.setEnabled((!username.getText().toString().isEmpty())&&
                        (!password.getText().toString().isEmpty())&&
                        (!serverhost.getText().toString().isEmpty())&&
                        (!serverport.getText().toString().isEmpty())&&
                        (!firstname.getText().toString().isEmpty())&&
                        (!lastname.getText().toString().isEmpty())&&
                        (!email.getText().toString().isEmpty())&&
                        (Gender.equals("m")||Gender.equals("f")));
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        };

        //getting inputs
        serverhost.addTextChangedListener(textWatcher);
        serverport.addTextChangedListener(textWatcher);
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        firstname.addTextChangedListener(textWatcher);
        lastname.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);

        //the way to check which button is pressed
        radioGroup.setOnCheckedChangeListener((radioGroup1, i) -> {
            //getting reference
            RadioButton maleButton = view.findViewById(R.id.Male);
            RadioButton femaleButton = view.findViewById(R.id.Female);
            if(maleButton.isChecked()){
                Gender = "m";
            }
            else if(femaleButton.isChecked()){
                Gender = "f";
            }
        });

        //login button is clicked!
        buttonL.setOnClickListener(v -> {
            // Set up a handler that will process messages from the task and make updates on UI Thread
            Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message message){
                    //get the task result
                    Bundle bundle = message.getData();
                    Boolean result = bundle.getBoolean(FINAL_RESULT);
                    if(result){
                        //get the first name and last name
                        String FirstName = person.getFirstName();
                        String LastName = person.getLastName();
                        Toast.makeText(getActivity(), "Welcome, "+ FirstName + " " + LastName + ".", Toast.LENGTH_LONG).show();
                        ((MainActivity) getActivity()).loginTOmap();
                    }else Toast.makeText(getActivity(), "failed Login. Try again", Toast.LENGTH_LONG).show();
                }
            };

            //Create an execute the download task on a separate thread
            LoginTask task = new LoginTask(uiThreadMessageHandler, serverhost.getText().toString(),
                    serverport.getText().toString(), username.getText().toString(), password.getText().toString());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task);
        });

        //register button is clicked!
        buttonR.setOnClickListener(v -> {
            // Set up a handler that will process messages from the task and make updates on UI Thread
            Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message message){
                    //get the task result
                    Bundle bundle = message.getData();
                    Boolean result = bundle.getBoolean(FINAL_RESULT);
                    if(result){
                        //get the first name and last name
                        String FirstName = person.getFirstName();
                        String LastName = person.getLastName();
                        Toast.makeText(getActivity(), "Welcome, "+ FirstName + " " + LastName + ".", Toast.LENGTH_LONG).show();
                    }else Toast.makeText(getActivity(), "failed Register. Try again", Toast.LENGTH_LONG).show();
                }
            };

            //Create an execute the download task on a separate thread
            RegisterTask task = new RegisterTask(uiThreadMessageHandler, serverhost.getText().toString(),
                    serverport.getText().toString(), username.getText().toString(), password.getText().toString(),
                    firstname.getText().toString(), lastname.getText().toString(), email.getText().toString(),
                    Gender);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task);
        });
        //setting things makes things easier
        serverhost.setText("10.0.2.2");
        serverport.setText("8080");
        username.setText("sheila");
        password.setText("parker");
        return view;
    }

    private static class LoginTask implements Runnable{
        private final Handler messageHanlder;
        private final String ServerHost;
        private final String ServerPost;
        private final String userName;
        private final String password;
        //constructor
        public LoginTask(Handler messageHandler, String serverHost, String serverPost, String userName, String password) {
            this.messageHanlder = messageHandler;
            this.ServerHost = serverHost;
            this.ServerPost = serverPost;
            this.userName = userName;
            this.password = password;
        }

        //main function
        @Override
        public void run() {
            //get the datacache and serverproxy
            DataCache data = DataCache.getInstance();
            ServerProxy sp = new ServerProxy();
            //try to login
            LoginRequest request= new LoginRequest(userName, password);
            LoginResult result = sp.loginResult(request, ServerHost, ServerPost);
            String auth = result.getAuthtoken();

            if(result.isSuccess()){
                //grap the info for the user
                data.setPeople(sp.personResult(ServerHost,ServerPost, auth).getData());
                data.setEvents(sp.eventResult(ServerHost,ServerPost, auth).getData());
                person = data.findPerson(result.getPersonID());
                data.setUserPersonID(result.getPersonID());
            }
            sendMessage(result);
        }
        private void sendMessage(LoginResult result){
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(FINAL_RESULT, result.isSuccess());
            message.setData(messageBundle);
            messageHanlder.sendMessage(message);
        }

    }

    private static class RegisterTask implements Runnable{
        private final Handler messageHanlder;
        private final String ServerHost;
        private final String ServerPost;
        private final String userName;
        private final String password;
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String gender;
        //constructor
        public RegisterTask(Handler messageHanlder, String serverHost, String serverPost, String userName, String password, String firstName, String lastName, String email, String gender) {
            this.messageHanlder = messageHanlder;
            ServerHost = serverHost;
            ServerPost = serverPost;
            this.userName = userName;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.gender = gender;
        }
        //main function
        @Override
        public void run() {
            //get the datacache and serverproxy
            DataCache data =  DataCache.getInstance();
            ServerProxy sp = new ServerProxy();
            //try to register
            RegisterRequest request= new RegisterRequest(userName, password, email, firstName, lastName, gender);
            RegisterResult result = sp.registerResult(request, ServerHost, ServerPost);
            String auth = result.getAuthtoken();
            if(result.isSuccess()){
                //grap the info for the user
                data.setPeople(sp.personResult(ServerHost,ServerPost, auth).getData());
                data.setEvents(sp.eventResult(ServerHost,ServerPost, auth).getData());
                person = data.findPerson(result.getPersonID());
                data.setUserPersonID(result.getPersonID());
            }
            sendMessage(result);
        }

        private void sendMessage(RegisterResult result){
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(FINAL_RESULT, result.isSuccess());
            message.setData(messageBundle);
            messageHanlder.sendMessage(message);
        }

    }

}


// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyBADaqYisRBzkRtSQbqCb66V9PLyWZfQSY",
    authDomain: "recieppy.firebaseapp.com",
    databaseURL: "https://recieppy.firebaseio.com",
    projectId: "recieppy",
    messagingSenderId: "61879352730",
    appId: "1:61879352730:web:12af6c6047e91cce6e8e23"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
firebase.auth().useDeviceLanguage();
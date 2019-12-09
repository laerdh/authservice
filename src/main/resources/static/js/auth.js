// Firebase UI
let ui = new firebaseui.auth.AuthUI(firebase.auth());
let uiConfig = {
    callbacks: {
        signInSuccessWithAuthResult: (authResult, redirectUrl) => {
            let user = authResult.user;
            let form = document.getElementById("loginForm");
            document.getElementById("username").value = user.phoneNumber;
            document.getElementById("password").value = user.uid;
            form.submit();
            return true;
        }
    },
    signInOptions: [
        {
            provider: firebase.auth.PhoneAuthProvider.PROVIDER_ID,
            recaptchaParameters: {
                type: 'image',
                size: 'normal',
                badge: 'bottomleft'
            },
            defaultCountry: 'NO'
        }
    ]
};
ui.start('#firebaseui-auth-container', uiConfig);
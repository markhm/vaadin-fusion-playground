import { setCookie, getCookie } from "../../../frontend/utils/security";

console.log('something');

setCookie("user_email","bobthegreat@gmail.com",30); //set "user_email" cookie, expires in 30 days

var userEmail = getCookie("user_email");//"bobthegreat@gmail.com"

console.log('getCookie is: ' + userEmail);

export function setCookie(name: string, value: string, days: number) {
    let expires = "";
    if (days) {
        let date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}

export function getCookie(name: string) {
    let nameEQ = name + "=";
    let ca = document.cookie.split(';');
    for(let i = 0 ; i < ca.length ; i++) {
        let c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

export function authenticate(username: string, token: string) {
    setCookie(username, token, 30);
}

export function isAuthenticated(name: string) {
    let cookie = getCookie(name);

    if (!cookie || cookie === "") {
        console.log('No valid cookie found. User is nót authenticated');
        return false;
    }
    else {
        console.log('Valid cookie found. User is authenticated');
        return true;
    }
}

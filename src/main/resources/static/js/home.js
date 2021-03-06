// 获取相关用户信息

const titleDom = document.querySelector(".weui-form__title");
const result = document.getElementById("result");
const result2 = document.getElementById("result2");

// getUserInfo
function getUserInfo() {
    dataService.getUserInfo().then(res => {
        const {code, data} = res;
        if (code === 401) {
            location.href = location.origin + "/login.html";
            return;
        } else if (code == 1001) {
            alert("你的账户已经在别的设备登录");
            location.href = location.origin + "/login.html";
            return;
        }
        if (data) {
            titleDom.innerHTML = 'Hello ' + data.userName + ', 欢迎登录追梦空间';
            createSocket({
                sessionId: data.uuid,
                userId: data.userId
            })
            createSocket2({
                sessionId: data.uuid,
                routeCode: "d3ulY7HyJmfscUBCvHDX4dBnV8Te5ZK4orhtzbSf0e0="
            })
        }

    })
}


// websocket 连接

let socket;
let socket2;

const createSocket = (params) => {
    if (typeof WebSocket == 'undefined') {
        console.log("浏览器不支持websocket");
    } else {
        const paramsArr = [];
        Object.keys(params).forEach(m => {
            paramsArr.push(`${m}=${params[m]}`);
        });
        const sessionId = params['sessionId'];
        const userId = params['userId'];
        let socketUrl = location.origin + "/message?" + paramsArr.join("&");
        socketUrl = socketUrl.replace(/http|https/g, 'ws');
        console.log(socketUrl);
        if (socket != null) {
            socket.close();
            socket = null;
        }
        socket = new WebSocket(socketUrl);
        // 建立连接
        socket.onopen = () => {
            console.log("建立连接", sessionId);

            socket.send(JSON.stringify({
                sessionId: sessionId,
                query: 'onLineNumber'
            }));

        };
        // 获取消息
        socket.onmessage = message => {
            console.log(sessionId, message);
            const data = JSON.parse(message.data);
            if (data.code == 1001) {
                // 踢出通知
                alert(data.message);
                location.href = location.origin + "/login.html";
            } else if (data.code == 200) {
                result.innerText = data.message;
            }
        };

    }
};

const createSocket2 = (params) => {
    if (typeof WebSocket == 'undefined') {
        console.log("浏览器不支持websocket");
    } else {
        const paramsArr = [];
        Object.keys(params).forEach(m => {
            paramsArr.push(`${m}=${params[m]}`);
        });
        const sessionId = params['sessionId'];
        let socketUrl = location.origin + "/liveBus?" + paramsArr.join("&");
        socketUrl = socketUrl.replace(/http|https/g, 'ws');
        console.log(socketUrl);
        if (socket2 != null) {
            socket2.close();
            socket2 = null;
        }
        socket2 = new WebSocket(socketUrl);
        // 建立连接
        socket2.onopen = () => {
            console.log("建立连接2", sessionId);

            socket2.send(JSON.stringify({
                sessionId: sessionId,
                routeCode: "d3ulY7HyJmfscUBCvHDX4dBnV8Te5ZK4orhtzbSf0e0="
            }));

        };
        // 获取消息
        socket2.onmessage = message => {
            console.log(sessionId, message);
            const data = JSON.parse(message.data);
            console.log("连接2");
            console.log(data)
            result2.innerText = JSON.stringify(data);
        };

    }
};


getUserInfo();
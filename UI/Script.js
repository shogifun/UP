
'use strict';
var nickName;
var id=1;
var messageList;
var isNickNameChoose=false;
var Application = {
    mainUrl : 'http://localhost:8080/chat',
    messageList : [],
    token : 'TN11EN',
    isConnected : null
};
function run() {

    //messageList=loadFromStorage();

    //nickName=getNicknameFromStorage();


    var changeNickName=document.getElementById('changeNickname');
    changeNickName.addEventListener('click',delegateEvent);
    var sendMessage=document.getElementById('send');
    sendMessage.addEventListener('click',delegateEvent);
    getHistroryFromServer();
    nickName=Application.messageList[Application.messageList.length-1];
    if(nickName==null) nickName='unknown user';
    var header=document.getElementById('h2');
    header.innerHTML='Your current nickname is '+nickName;
    /*if(messageList!=null)
        render();
    else
        messageList=[];*/

}
function render(messageList) {
    clearDivList();
    var divList= document.getElementById('messageList');
    for(var i=0;i<messageList.length;i++)
    //for(var i=0;i<Application.messageList.length;i++)
    {

        var newDiv=createDiv(messageList[i]);
        //var newDiv=createDiv(Application.messageList[i]);
        divList.appendChild(newDiv)
    }

}
function delegateEvent(evtObj) {


    switch (this.className){
        case 'controlButtons':
            var message=createMessage();
            if(!message){

                return ;
            }
            var newDiv=createDiv(message);
            var divList= document.getElementById('messageList');
            divList.appendChild(newDiv);
            Application.messageList.push(message);
            postMessage(message);
            //saveToStorage();
            break;
        case 'changeNickname':
            changeNickName();
            //saveToStorage();

            break;
        case 'deleteButton':
            deleteMessage(this.id);
            //saveToStorage();
            break;
        case 'changeButton':
            changeMessage(this.id);
            //saveToStorage();
            break;
        default: break;

    }


}
function changeMessage(id){

    var div=document.getElementById(id).parentNode;
    var res=0;
    for(var i=0;i<Application.messageList.length;i++)
        if(Application.messageList[i].id==div.id){
            res=i;
            break;
        }

    while(div.firstChild){
        div.removeChild(div.firstChild);
    }

    var textArea=document.createElement('textarea');
    textArea.setAttribute('placeholder','Input message');
    div.appendChild(textArea);

    var okButton=document.createElement('button');
    okButton.innerHTML='change';
    okButton.className='controlButtons';
    okButton.addEventListener('click',onOk);
    div.appendChild(okButton);

    var cancelButton=document.createElement('button');
    cancelButton.className='controlButtons';
    cancelButton.innerHTML='cancel';
    cancelButton.addEventListener('click',onCancel);
    div.appendChild(cancelButton);



}
function deleteMessage(id){
    var div=document.getElementById(id).parentNode;
    var res=-1;
    for(var i=0;i<Application.messageList.length;i++)
        if(Application.messageList[i].id==div.id){
            res=i;
            break;
        }
    Application.messageList[res].isDeleted=true;
    deleteMessageFromServer(Application.messageList[res]);
    //messageList.splice(res,1);
    div.innerHTML='message was deleted';

}
function changeNickName(){
    var input=document.getElementById('inputNewNickname');
    nickName=input.value;
    input.value='';
    var header=document.getElementById('h2');
    header.innerHTML='Your current nickname is '+nickName;
    //saveToStorage();
    //clearDivList();
    render(Application.messageList);
}
function createMessage(){
    var  currentDate=new Date();
    var input=document.getElementById('newMessage');
    var text=input.value;
    if(!text.length){
        return false;
    }
    var message=new Message(text,currentDate,uniqueId(),nickName);
    input.value='';
    return message;
}
function createDiv(message){
    var div=document.createElement('div');
    if(nickName==message.nickName) {
        div.className = 'myMessage'
    }
    else
    {
        div.className='otherMessage';
    }

    div.id=message.id;
    if(!message.isDeleted) {
        var date = new Date(message.date);
        if(message.isChanged)
        {
            div.innerHTML = date.toDateString() + ' ' + date.toLocaleTimeString() + ' ' + message.nickName + ':' + message.text
                +' <i> This  message was changed</i>';
        }
        else {
            div.innerHTML = date.toDateString() + ' ' + date.toLocaleTimeString() + ' ' + message.nickName + ':' + message.text;

        }
        addButtons(div, message);
    }
    else
        {
            div.innerHTML='<i>Message was deleted</i>'
        }


    return div;



}
function addButtons(div,message){
    var deleteButton=document.createElement('button');
    deleteButton.innerHTML='delete';
    deleteButton.className='deleteButton';
    deleteButton.addEventListener('click',delegateEvent);
    deleteButton.id=uniqueId();
    div.appendChild(deleteButton);
    if(message.nickName==nickName) {
        var changeButton = document.createElement('button');
        changeButton.innerHTML = 'change';
        changeButton.className = 'changeButton';
        changeButton.id = uniqueId();
        changeButton.addEventListener('click', delegateEvent);
        div.appendChild(changeButton);
    }




}
function Message(text,date,id,nickname) {
    this.id=id;
    this.text=text;
    this.date=date;
    this.nickName=nickname;
    this.isChanged=false;
    this.isDeleted=false;
}
function saveToStorage() {
    localStorage.setItem("messageList",JSON.stringify(messageList));
    localStorage.setItem("nickName",nickName);
}
function loadFromStorage(){
    if(typeof(Storage) == "undefined") {

        alert('localStorage is not accessible');
        return;

    }

    var item = localStorage.getItem("messageList");

    return item && JSON.parse(item);

}
function getNicknameFromStorage(){
    if(typeof(Storage) == "undefined") {

        alert('localStorage is not accessible');
        return;

    }

    var item = localStorage.getItem("nickName");

    return item ;
}
function uniqueId() {

    var date = Date.now();
    var random =

        Math.random() * Math.random();

    return ' '+Math.floor(date * random);

}
function clearDivList(){
    var divList= document.getElementById('messageList');
    while(divList.firstChild){
        divList.removeChild(divList.firstChild);
    }

}
function onCancel(){
    clearDivList();
    render(Application.messageList);

}
function onOk(){
    var div=this.parentNode;
    var input=div.firstChild;
    var text=input.value;
    while(div.firstChild){
        div.removeChild(div.firstChild);
    }
    var res=0;
    for(var i=0;i<Application.messageList.length;i++)
        if(Application.messageList[i].id==div.id){
            res=i;
            break;
        }
    //var text=input.value;
    Application.messageList[res].text=text;
    Application.messageList[res].isChanged=true;
    clearDivList();
    render(Application.messageList);
    putMessage(Application.messageList[res]);

    //saveToStorage();
}
function ajax(method, url, data, continueWith, continueWithError) {
    var xhr = new XMLHttpRequest();

    continueWithError = continueWithError || defaultErrorHandler;
    xhr.open(method || 'GET', url, true);

    xhr.onload = function () {
        if (xhr.readyState !== 4)
            return;

        if(xhr.status != 200) {
            continueWithError('Error on the server side, response ' + xhr.status);
            return;
        }

        if(isError(xhr.responseText)) {
            continueWithError('Error on the server side, response ' + xhr.responseText);
            return;
        }

        continueWith(xhr.responseText);
        Application.isConnected = true;
    };

    xhr.ontimeout = function () {
        ServerError();
    }

    xhr.onerror = function (e) {
        ServerError();
    };

    xhr.send(data);
}
function defaultErrorHandler(message) {
    console.error(message);
    //output(message);
}
function isError(text) {
    if(text == "")
        return false;

    try {
        var obj = JSON.parse(text);
    } catch(ex) {
        return true;
    }

    return !!obj.error;
}
function ServerError(){
    //var errorServer = document.getElementsByClassName('ServerError')[0];
    //errorServer.innerHTML = '<img class="alarm" align="right" src="alarm.png" alt="Connection problems">';
    var image=document.getElementById('noConnection');
    image.visibility=true;
}
function Connect() {
    if(Application.isConnected)
        return;

    function whileConnected() {
        Application.isConnected = setTimeout(function () {
            ajax('GET', Application.mainUrl + '?token=' + Application.token, null,function (serverResponse) {
                if (Application.isConnected) {
                    var json = JSON.parse(serverResponse);
                    Application.messageList=[];
                    Application.messageList = createMessageList(json);
                    render(Application.messageList);
                    whileConnected();
                }
            });
        }, Math.round(7000));
    }

    whileConnected();
}
function getHistroryFromServer(){
    var url = Application.mainUrl + '?token=' + Application.token;
    ajax('GET', url, null, function(responseText){
        var json = JSON.parse(responseText);
        Application.messageList=[];
        Application.messageList = createMessageList(json);
        render(Application.messageList);
        Connect();
    });
}
function createMessageList(json){
    var res=[];
    for(var i=0;i<json.messages.length;i++){
        var date=new Date(json.messages[i].timestamp)
        var text=json.messages[i].text;
        var nick=json.messages[i].author;
        var id=json.messages[i].id
        var change=false;
        var del=false;

        if(json.messages[i].isEdit=='was edited')  change=true;
        if(json.messages[i].text=='message was deleted') del=true;
        var message=new Message(text,date,id,nick);
        message.isChanged=change;
        message.isDeleted=del;
        res.push(message);
    }
    return res;

}
function putMessage(message) {
    ajax('PUT', Application.mainUrl, JSON.stringify(transformMessage(message)), function () {
    });
}
function deleteMessageFromServer(message){
    ajax('DELETE', Application.mainUrl, JSON.stringify(transformMessage(message)), function () {
    });
}
function postMessage(message){

    ajax('POST', Application.mainUrl, JSON.stringify(transformMessage(message)), function () {
    });
}
function transformMessage(message){
    var edit=''
    if(message.isChanged)
        edit='was edited'
    return{
        author: message.nickName,
        text: message.text,
        id: message.id,
        timestamp: message.date.getTime(),
        isEdit: edit
    }
}
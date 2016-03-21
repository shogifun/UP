/**
 * Created by user on 07.03.2016.
 */
var nickName;
var id=1;
var messageList;
function run() {

    messageList=loadFromStorage();

    nickName=getNicknameFromStorage();
    if(nickName==null) nickName='unknown user'
    var header=document.getElementById('h2');
    header.innerHTML='Your current nickname is '+nickName;

    var changeNickName=document.getElementById('changeNickname');
    changeNickName.addEventListener('click',delegateEvent);
    var sendMessage=document.getElementById('send');
    sendMessage.addEventListener('click',delegateEvent);

    if(messageList!=null)
        render();
    else
        messageList=[];

}
function render() {

    var divList= document.getElementById('messageList');

    for(var i=0;i<messageList.length;i++)
    {

        var newDiv=createDiv(messageList[i]);
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
            messageList.push(message);
            saveToStorage();
            break;
        case 'changeNickname':
            changeNickName();
            saveToStorage();

            break;
        case 'deleteButton':
            deleteMessage(this.id);
            saveToStorage();
            break;
        case 'changeButton':
            changeMessage(this.id);
            saveToStorage();
            break;
        default: break;

    }


}
function changeMessage(id){
    var div=document.getElementById(id).parentNode;
    var res=0;
    for(var i=0;i<messageList.length;i++)
        if(messageList[i].id==div.id){
            var messageForChange=messageList[i];
            res=i;
            break;
        }
    var changedText=prompt('Enter changing in message:'+messageForChange.text);
    if((changedText==null)||(changedText=='')) return;
    messageForChange.text=changedText;
    messageList[res].text=changedText;
    messageList[res].isChanged=true;
    var date = new Date(messageForChange.date);
    div.innerHTML = date.toDateString() + ' ' + date.toLocaleTimeString() + ' ' + messageForChange.nickName + ':' +
        messageForChange.text+' <i> This  message was changed</i>';
    addButtons(div,messageList[res]);

}
function deleteMessage(id){
    var div=document.getElementById(id).parentNode;
    var res=-1;
    for(var i=0;i<messageList.length;i++)
        if(messageList[i].id==div.id){
            res=i;
            break;
        }
    messageList[res].isDeleted=true;
    //messageList.splice(res,1);
    div.innerHTML='message was deleted';

}
function changeNickName(){
    var input=document.getElementById('inputNewNickname');
    nickName=input.value;
    input.value='';
    var header=document.getElementById('h2');
    header.innerHTML='Your current nickname is '+nickName;
    saveToStorage();
    clearDivList();
    render();
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

    return Math.floor(date * random);

}
function clearDivList(){
    var divList= document.getElementById('messageList');
    while(divList.firstChild){
        divList.removeChild(divList.firstChild);
    }

}
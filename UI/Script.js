/**
 * Created by user on 07.03.2016.
 */
var nickName='Stiv';
var id=1;
var messageId=1;
var flag=true;
var messageList=[{id:'wee',className:'otherMessage',date:1457460562751,
text:'gygyugugiu',nickName:'hjhjhj'},{id:'opi',className:'otherMessage',date:1457460562751,
    text:'gygyukkku',nickName:'jjkpphj'}];
function run()
{


    var changeNickName=document.getElementById('changeNickname');
    changeNickName.addEventListener('click',delegateEvent);
    var sendMessage=document.getElementById('send');
    sendMessage.addEventListener('click',delegateEvent);
    for(var i=0;i<messageList.length;i++)
    {
        var newDiv=createDiv(messageList[i]);
        var divList= document.getElementById('messageList');
        divList.appendChild(newDiv)
    }
    flag=false;
}
function delegateEvent(evtObj)
{


    switch (this.className){
        case 'controlButtons':
            var message=createMessage();
            if(!message){
                alert('Enter message');
                return ;
            }
            var newDiv=createDiv(message);
            var divList= document.getElementById('messageList');
            divList.appendChild(newDiv)
            messageList.push(message);
            break;
        case 'changeNickname':
            changeNickName();
            break;
        case 'deleteButton':
            deleteMessage(this.id);
            break;
        case 'changeButton':
            changeMessage(this.id);
            break;
        default: break;

    }


}
function changeMessage(id){
    var div=document.getElementById(id).parentNode;
    for(var i=0;i<messageList.length;i++)
        if(messageList[i].id==div.id){
            var messageForChange=messageList[i];
            break;
        }
    var changedText=prompt('Enter changing in message:'+messageForChange.text);
    if((changedText==null)||(changedText=='')) return;
    messageForChange.text=changedText;
    div.innerHTML=messageForChange.date.toDateString()+' '+messageForChange.date.toLocaleTimeString()+' '+
        messageForChange.nickName+':'+ messageForChange.text;
    addButtons(div);

}
function deleteMessage(id){
    var div=document.getElementById(id).parentNode;
    var res=-1;
    for(var i=0;i<messageList.length;i++)
        if(messageList[i].id==div.id){
            res=i;
            break;
        }
    if(res==-1) alert('Fuck');
    messageList.slice(i,1);
    div.remove();

}
function changeNickName(){
    var input=document.getElementById('inputNewNickname');
    nickName=input.value;
    input.value='';
    var header=document.getElementById('h2');
    header.innerHTML='Your current nickname is '+nickName;
}
function createMessage(){
    var  currentDate=new Date();
    var input=document.getElementById('newMessage');
    var text=input.value;
    if(!text.length){
        return false;
    }
    var message=new Message(text,currentDate,'myMessage',messageId,nickName);
    messageId++;
    input.value='';
    return message;
}
function createDiv(message){
    var div=document.createElement('div');
    div.className=message.className;
    div.id=message.id;
    if(flag){
        var date=new Date(message.date);
        div.innerHTML=date.toDateString()+' '+date.toLocaleTimeString()+' '+message.nickName+':'+ message.text;
    }
    else
        div.innerHTML=message.date.toDateString()+' '+message.date.toLocaleTimeString()+' '+message.nickName+':'+ message.text;

    addButtons(div);
    return div;



}
function addButtons(div){
    var deleteButton=document.createElement('button');
    deleteButton.innerHTML='delete';
    deleteButton.className='deleteButton';
    deleteButton.addEventListener('click',delegateEvent);
    deleteButton.id=id;
    id++;
    div.appendChild(deleteButton);
    if(!flag) {
        var changeButton = document.createElement('button');
        changeButton.innerHTML = 'change';
        changeButton.className = 'changeButton';
        changeButton.id = id;
        id++;
        changeButton.addEventListener('click', delegateEvent);
        div.appendChild(changeButton);
    }




}
function Message(text,date,className,id,nickname) {
    this.id=id+'a';
    this.text=text;
    this.date=date;
    this.className=className;
    this.nickName=nickname;
}
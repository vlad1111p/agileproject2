const chatButton = document.querySelector('.chatbox_toggle');
const chatContent = document.querySelector('.chatbox_overview');

const icons = {
    isClicked: '<i class="bi bi-envelope" style="font-size:2em;' +
        'color: #01588d; ' +
        'background-color: #fff;' +
        'border-radius: 5px;' +
        'padding-right: 3px;' +
        'padding-left: 3px;' +
        'margin-left: 3px;' +
        'margin-right: 3px;"></i>',
    isNotClicked: '<a id="chats"><span><i class="bi bi-envelope" style="font-size: 2rem"></i></span></a>'
    /*<a href="/meineChats" th:href="@{/meineChats}"<i class="bi bi-envelope navbarIcon"></i></a>*/
}
const chatbox = new InteractiveChatbox(chatButton, chatContent, icons);
chatbox.display();
chatbox.toggleIcon(false, chatButton);

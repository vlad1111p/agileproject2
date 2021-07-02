const chatButton = document.querySelector('.chatbox_toggle');
const chatContent = document.querySelector('.chatbox_overview');

const icons = {
    isClicked: '<a><i class="bi bi-envelope navbarIcon" style="color:#BF1515"></i></a>',
    isNotClicked: '<a><i class="bi bi-envelope navbarIcon" style="color:#fff"></i></a>'
    /*<a href="/meineChats" th:href="@{/meineChats}"<i class="bi bi-envelope navbarIcon"></i></a>*/
}
const chatbox = new InteractiveChatbox(chatButton, chatContent, icons);
chatbox.display();
chatbox.toggleIcon(false, chatButton);

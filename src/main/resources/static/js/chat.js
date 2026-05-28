window.addEventListener('load', function () {
    const chatBox = document.getElementById('chat-box');
    const chatForm = document.getElementById('chat-form');
    const messageInput = document.getElementById('message-input');

    if (!chatBox || !chatForm || !messageInput) {
        return;
    }

    const responseId = chatBox.dataset.responseId;

    const socket = new SockJS('/ws');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        stompClient.subscribe('/topic/chat/' + responseId, function (message) {
            const messageData = JSON.parse(message.body);
            appendMessage(messageData);
        });
    });

    chatForm.addEventListener('submit', function (event) {
        event.preventDefault();

        const content = messageInput.value.trim();

        if (!content) {
            return;
        }

        stompClient.send('/app/chat/send', {}, JSON.stringify({
            respondedApplicantId: responseId,
            content: content
        }));

        messageInput.value = '';
    });

    function appendMessage(messageData) {
        const emptyMessage = document.getElementById('empty-chat-message');

        if (emptyMessage) {
            emptyMessage.remove();
        }

        const messageBlock = document.createElement('div');
        messageBlock.className = 'mb-3';

        const senderBlock = document.createElement('div');
        senderBlock.className = 'fw-semibold';
        senderBlock.textContent = messageData.senderName;

        const contentBlock = document.createElement('div');
        contentBlock.textContent = messageData.content;

        const timeBlock = document.createElement('div');
        timeBlock.className = 'small text-muted';
        timeBlock.textContent = messageData.timestamp;

        messageBlock.appendChild(senderBlock);
        messageBlock.appendChild(contentBlock);
        messageBlock.appendChild(timeBlock);

        chatBox.appendChild(messageBlock);
        chatBox.scrollTop = chatBox.scrollHeight;
    }
});
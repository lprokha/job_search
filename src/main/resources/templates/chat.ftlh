<#import "layouts/layout.ftlh" as main>
<#import "/spring.ftl" as spring>

<@main.layout>
    <div class="row justify-content-center">
        <div class="col-lg-8">

            <div class="card shadow-sm border-0 mb-3">
                <div class="card-body">
                    <h2 class="mb-3">
                        <@spring.message "chat.title"/>
                    </h2>

                    <p class="mb-1">
                        <span class="fw-semibold"><@spring.message "vacancy.title"/>:</span>
                        ${response.vacancy.name!""}
                    </p>

                    <p class="mb-1">
                        <span class="fw-semibold"><@spring.message "resume.name"/>:</span>
                        ${response.resume.name!""}
                    </p>

                    <p class="mb-0 text-muted">
                        <@spring.message "chat.responseId"/>: ${response.id}
                    </p>
                </div>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body">

                    <div id="chat-box"
                         class="border rounded p-3 bg-light mb-3"
                         style="min-height: 300px;"
                         data-response-id="${response.id}">

                        <#if messages?? && (messages?size > 0)>
                            <#list messages as message>
                                <div class="mb-3">
                                    <div class="fw-semibold">
                                        <#if message.sender??>
                                            ${message.sender.name} ${message.sender.surname}
                                        <#else>
                                            <@spring.message "chat.unknownSender"/>
                                        </#if>
                                    </div>

                                    <div>${message.content}</div>

                                    <#if message.timestamp??>
                                        <div class="small text-muted">
                                            ${message.timestamp}
                                        </div>
                                    </#if>
                                </div>
                            </#list>
                        <#else>
                            <p class="text-muted mb-0" id="empty-chat-message">
                                <@spring.message "chat.empty"/>
                            </p>
                        </#if>
                    </div>

                    <form id="chat-form" class="d-flex gap-2">
                        <input type="text"
                               id="message-input"
                               class="form-control"
                               placeholder="<@spring.message 'chat.messagePlaceholder'/>">

                        <button type="submit" class="btn btn-dark">
                            <@spring.message "button.send"/>
                        </button>
                    </form>
                </div>
            </div>

            <div class="mt-3">
                <a href="/profile" class="btn btn-outline-dark">
                    <@spring.message "button.backToProfile"/>
                </a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <script src="/static/js/chat.js"></script>
</@main.layout>
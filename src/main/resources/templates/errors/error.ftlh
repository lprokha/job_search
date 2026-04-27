<#import "../layouts/layout.ftlh" as main>

<@main.layout>
    <link rel="stylesheet" href="/css/error_styles.css">

    <div class="error_block">
        <div class="main_block">
            <div class="content">
                <h1>${status!}</h1>
                <h2>${reason!}</h2>

                <#if details??>
                    <ul class="details">
                        <#if details.serverName??>
                            <li><span>Host:</span> <code>${details.serverName}</code></li>
                        </#if>
                        <#if details.requestURL??>
                            <li><span>Original URI:</span> <code>${details.requestURL}</code></li>
                        </#if>
                        <#if details.serverPort??>
                            <li><span>Service port:</span> <code>${details.serverPort}</code></li>
                        </#if>
                        <#if details.requestId??>
                            <li><span>Request ID:</span> <code>${details.requestId}</code></li>
                        </#if>
                        <li><span>Timestamp:</span> <code>${.now}</code></li>
                    </ul>
                </#if>

                <a href="/vacancies" class="btn btn-dark mt-3">На главную</a>
            </div>
        </div>
    </div>

    <script src="/js/error_js.js"></script>
</@main.layout>
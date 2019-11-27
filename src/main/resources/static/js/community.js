/*
提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code === 200) {
                $("#comment_section").hide();
                window.location.reload();
            } else {
                if (response.code === 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=7850db15468cc2a16d28&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

//日期格式化
Date.prototype.Format = function(fmt)
{ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length===1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
};


/*
展开二级评论
 */

function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    var collapse = e.getAttribute("data-collapse");//获取当前折叠状态
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        if(subCommentContainer.children().length!==1){
            comments.addClass("in");//展开二级评论
            e.setAttribute("data-collapse", "in");//标记状态
            e.classList.add("active");
        }else {
            //追加二级评论
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data, function (index, comment) {
                    var mediaLeft = $("<div/>",{
                        "class":"media-left"
                    }).append($("<img/>",{
                        "class":"img-rounded media-object",
                        "src":comment.user.avatarUrl
                    }));

                    var bodyElement = $("<div/>",{
                        "class":"media-body"
                    }).append($("<h5/>",{
                        "class":"media-heading",
                        "id":"secComment-"+comment.id,
                        html:comment.user.name
                    })).append($("<div/>",{
                        html:comment.content
                    })).append($("<div/>",{
                        "class":"menu",
                    }).append($("<div/>",{
                        "class":"pull-right",
                        html:new Date(comment.gmtCreate).Format("yyyy-MM-dd hh:mm")+" <a style='cursor: pointer;' onclick='commentToCommentator("+id+","+comment.id+")'>回复</a>"
                    })));

                    var mediaElement = $("<div/>",{
                        "class":"media",
                    }).append(mediaLeft).append(bodyElement);

                    var commentElement = $("<div/>", {
                        "class": "media col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                comments.addClass("in");//展开二级评论
                e.setAttribute("data-collapse", "in");//标记状态
                e.classList.add("active");
            });
        }
    }
}

//二级回复
function commentToCommentator(id,commentId) {
    console.log(id);
    var inputBtn = $("#input-"+id);
    var name = $("#secComment-"+commentId).html();
    inputBtn.attr("value","回复 "+name+" :")
}


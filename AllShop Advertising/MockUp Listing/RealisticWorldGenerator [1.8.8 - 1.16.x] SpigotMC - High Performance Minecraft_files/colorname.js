var swfobject=function(){function C(){if(b){return}try{var e=a.getElementsByTagName("body")[0].appendChild(U("span"));e.parentNode.removeChild(e)}catch(t){return}b=true;var n=c.length;for(var r=0;r<n;r++){c[r]()}}function k(e){if(b){e()}else{c[c.length]=e}}function L(t){if(typeof u.addEventListener!=e){u.addEventListener("load",t,false)}else if(typeof a.addEventListener!=e){a.addEventListener("load",t,false)}else if(typeof u.attachEvent!=e){z(u,"onload",t)}else if(typeof u.onload=="function"){var n=u.onload;u.onload=function(){n();t()}}else{u.onload=t}}function A(){if(l){O()}else{M()}}function O(){var n=a.getElementsByTagName("body")[0];var r=U(t);r.setAttribute("type",i);var s=n.appendChild(r);if(s){var o=0;(function(){if(typeof s.GetVariable!=e){var t=s.GetVariable("$version");if(t){t=t.split(" ")[1].split(",");T.pv=[parseInt(t[0],10),parseInt(t[1],10),parseInt(t[2],10)]}}else if(o<10){o++;setTimeout(arguments.callee,10);return}n.removeChild(r);s=null;M()})()}else{M()}}function M(){var t=h.length;if(t>0){for(var n=0;n<t;n++){var r=h[n].id;var i=h[n].callbackFn;var s={success:false,id:r};if(T.pv[0]>0){var o=R(r);if(o){if(W(h[n].swfVersion)&&!(T.wk&&T.wk<312)){V(r,true);if(i){s.success=true;s.ref=_(r);i(s)}}else if(h[n].expressInstall&&D()){var u={};u.data=h[n].expressInstall;u.width=o.getAttribute("width")||"0";u.height=o.getAttribute("height")||"0";if(o.getAttribute("class")){u.styleclass=o.getAttribute("class")}if(o.getAttribute("align")){u.align=o.getAttribute("align")}var a={};var f=o.getElementsByTagName("param");var l=f.length;for(var c=0;c<l;c++){if(f[c].getAttribute("name").toLowerCase()!="movie"){a[f[c].getAttribute("name")]=f[c].getAttribute("value")}}P(u,a,r,i)}else{H(o);if(i){i(s)}}}}else{V(r,true);if(i){var p=_(r);if(p&&typeof p.SetVariable!=e){s.success=true;s.ref=p}i(s)}}}}}function _(n){var r=null;var i=R(n);if(i&&i.nodeName=="OBJECT"){if(typeof i.SetVariable!=e){r=i}else{var s=i.getElementsByTagName(t)[0];if(s){r=s}}}return r}function D(){return!w&&W("6.0.65")&&(T.win||T.mac)&&!(T.wk&&T.wk<312)}function P(t,n,r,i){w=true;g=i||null;y={success:false,id:r};var o=R(r);if(o){if(o.nodeName=="OBJECT"){v=B(o);m=null}else{v=o;m=r}t.id=s;if(typeof t.width==e||!/%$/.test(t.width)&&parseInt(t.width,10)<310){t.width="310"}if(typeof t.height==e||!/%$/.test(t.height)&&parseInt(t.height,10)<137){t.height="137"}a.title=a.title.slice(0,47)+" - Flash Player Installation";var f=T.ie&&T.win?"ActiveX":"PlugIn",l="MMredirectURL="+u.location.toString().replace(/&/g,"%26")+"&MMplayerType="+f+"&MMdoctitle="+a.title;if(typeof n.flashvars!=e){n.flashvars+="&"+l}else{n.flashvars=l}if(T.ie&&T.win&&o.readyState!=4){var c=U("div");r+="SWFObjectNew";c.setAttribute("id",r);o.parentNode.insertBefore(c,o);o.style.display="none";(function(){if(o.readyState==4){o.parentNode.removeChild(o)}else{setTimeout(arguments.callee,10)}})()}j(t,n,r)}}function H(e){if(T.ie&&T.win&&e.readyState!=4){var t=U("div");e.parentNode.insertBefore(t,e);t.parentNode.replaceChild(B(e),t);e.style.display="none";(function(){if(e.readyState==4){e.parentNode.removeChild(e)}else{setTimeout(arguments.callee,10)}})()}else{e.parentNode.replaceChild(B(e),e)}}function B(e){var n=U("div");if(T.win&&T.ie){n.innerHTML=e.innerHTML}else{var r=e.getElementsByTagName(t)[0];if(r){var i=r.childNodes;if(i){var s=i.length;for(var o=0;o<s;o++){if(!(i[o].nodeType==1&&i[o].nodeName=="PARAM")&&!(i[o].nodeType==8)){n.appendChild(i[o].cloneNode(true))}}}}}return n}function j(n,r,s){var o,u=R(s);if(T.wk&&T.wk<312){return o}if(u){if(typeof n.id==e){n.id=s}if(T.ie&&T.win){var a="";for(var f in n){if(n[f]!=Object.prototype[f]){if(f.toLowerCase()=="data"){r.movie=n[f]}else if(f.toLowerCase()=="styleclass"){a+=' class="'+n[f]+'"'}else if(f.toLowerCase()!="classid"){a+=" "+f+'="'+n[f]+'"'}}}var l="";for(var c in r){if(r[c]!=Object.prototype[c]){l+='<param name="'+c+'" value="'+r[c]+'" />'}}u.outerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"'+a+">"+l+"</object>";p[p.length]=n.id;o=R(n.id)}else{var h=U(t);h.setAttribute("type",i);for(var d in n){if(n[d]!=Object.prototype[d]){if(d.toLowerCase()=="styleclass"){h.setAttribute("class",n[d])}else if(d.toLowerCase()!="classid"){h.setAttribute(d,n[d])}}}for(var v in r){if(r[v]!=Object.prototype[v]&&v.toLowerCase()!="movie"){F(h,v,r[v])}}u.parentNode.replaceChild(h,u);o=h}}return o}function F(e,t,n){var r=U("param");r.setAttribute("name",t);r.setAttribute("value",n);e.appendChild(r)}function I(e){var t=R(e);if(t&&t.nodeName=="OBJECT"){if(T.ie&&T.win){t.style.display="none";(function(){if(t.readyState==4){q(e)}else{setTimeout(arguments.callee,10)}})()}else{t.parentNode.removeChild(t)}}}function q(e){var t=R(e);if(t){for(var n in t){if(typeof t[n]=="function"){t[n]=null}}t.parentNode.removeChild(t)}}function R(e){var t=null;try{t=a.getElementById(e)}catch(n){}return t}function U(e){return a.createElement(e)}function z(e,t,n){e.attachEvent(t,n);d[d.length]=[e,t,n]}function W(e){var t=T.pv,n=e.split(".");n[0]=parseInt(n[0],10);n[1]=parseInt(n[1],10)||0;n[2]=parseInt(n[2],10)||0;return t[0]>n[0]||t[0]==n[0]&&t[1]>n[1]||t[0]==n[0]&&t[1]==n[1]&&t[2]>=n[2]?true:false}function X(n,r,i,s){if(T.ie&&T.mac){return}var o=a.getElementsByTagName("head")[0];if(!o){return}var u=i&&typeof i=="string"?i:"screen";if(s){E=null;S=null}if(!E||S!=u){var f=U("style");f.setAttribute("type","text/css");f.setAttribute("media",u);E=o.appendChild(f);if(T.ie&&T.win&&typeof a.styleSheets!=e&&a.styleSheets.length>0){E=a.styleSheets[a.styleSheets.length-1]}S=u}if(T.ie&&T.win){if(E&&typeof E.addRule==t){E.addRule(n,r)}}else{if(E&&typeof a.createTextNode!=e){E.appendChild(a.createTextNode(n+" {"+r+"}"))}}}function V(e,t){if(!x){return}var n=t?"visible":"hidden";if(b&&R(e)){R(e).style.visibility=n}else{X("#"+e,"visibility:"+n)}}function $(t){var n=/[\\\"<>\.;]/;var r=n.exec(t)!=null;return r&&typeof encodeURIComponent!=e?encodeURIComponent(t):t}var e="undefined",t="object",n="Shockwave Flash",r="ShockwaveFlash.ShockwaveFlash",i="application/x-shockwave-flash",s="SWFObjectExprInst",o="onreadystatechange",u=window,a=document,f=navigator,l=false,c=[A],h=[],p=[],d=[],v,m,g,y,b=false,w=false,E,S,x=true,T=function(){var s=typeof a.getElementById!=e&&typeof a.getElementsByTagName!=e&&typeof a.createElement!=e,o=f.userAgent.toLowerCase(),c=f.platform.toLowerCase(),h=c?/win/.test(c):/win/.test(o),p=c?/mac/.test(c):/mac/.test(o),d=/webkit/.test(o)?parseFloat(o.replace(/^.*webkit\/(\d+(\.\d+)?).*$/,"$1")):false,v=!+"1",m=[0,0,0],g=null;if(typeof f.plugins!=e&&typeof f.plugins[n]==t){g=f.plugins[n].description;if(g&&!(typeof f.mimeTypes!=e&&f.mimeTypes[i]&&!f.mimeTypes[i].enabledPlugin)){l=true;v=false;g=g.replace(/^.*\s+(\S+\s+\S+$)/,"$1");m[0]=parseInt(g.replace(/^(.*)\..*$/,"$1"),10);m[1]=parseInt(g.replace(/^.*\.(.*)\s.*$/,"$1"),10);m[2]=/[a-zA-Z]/.test(g)?parseInt(g.replace(/^.*[a-zA-Z]+(.*)$/,"$1"),10):0}}else if(typeof u.ActiveXObject!=e){try{var y=new ActiveXObject(r);if(y){g=y.GetVariable("$version");if(g){v=true;g=g.split(" ")[1].split(",");m=[parseInt(g[0],10),parseInt(g[1],10),parseInt(g[2],10)]}}}catch(b){}}return{w3:s,pv:m,wk:d,ie:v,win:h,mac:p}}(),N=function(){if(!T.w3){return}if(typeof a.readyState!=e&&a.readyState=="complete"||typeof a.readyState==e&&(a.getElementsByTagName("body")[0]||a.body)){C()}if(!b){if(typeof a.addEventListener!=e){a.addEventListener("DOMContentLoaded",C,false)}if(T.ie&&T.win){a.attachEvent(o,function(){if(a.readyState=="complete"){a.detachEvent(o,arguments.callee);C()}});if(u==top){(function(){if(b){return}try{a.documentElement.doScroll("left")}catch(e){setTimeout(arguments.callee,0);return}C()})()}}if(T.wk){(function(){if(b){return}if(!/loaded|complete/.test(a.readyState)){setTimeout(arguments.callee,0);return}C()})()}L(C)}}();var J=function(){if(T.ie&&T.win){window.attachEvent("onunload",function(){var e=d.length;for(var t=0;t<e;t++){d[t][0].detachEvent(d[t][1],d[t][2])}var n=p.length;for(var r=0;r<n;r++){I(p[r])}for(var i in T){T[i]=null}T=null;for(var s in swfobject){swfobject[s]=null}swfobject=null})}}();return{registerObject:function(e,t,n,r){if(T.w3&&e&&t){var i={};i.id=e;i.swfVersion=t;i.expressInstall=n;i.callbackFn=r;h[h.length]=i;V(e,false)}else if(r){r({success:false,id:e})}},getObjectById:function(e){if(T.w3){return _(e)}},embedSWF:function(n,r,i,s,o,u,a,f,l,c){var h={success:false,id:r};if(T.w3&&!(T.wk&&T.wk<312)&&n&&r&&i&&s&&o){V(r,false);k(function(){i+="";s+="";var p={};if(l&&typeof l===t){for(var d in l){p[d]=l[d]}}p.data=n;p.width=i;p.height=s;var v={};if(f&&typeof f===t){for(var m in f){v[m]=f[m]}}if(a&&typeof a===t){for(var g in a){if(typeof v.flashvars!=e){v.flashvars+="&"+g+"="+a[g]}else{v.flashvars=g+"="+a[g]}}}if(W(o)){var y=j(p,v,r);if(p.id==r){V(r,true)}h.success=true;h.ref=y}else if(u&&D()){p.data=u;P(p,v,r,c);return}else{V(r,true)}if(c){c(h)}})}else if(c){c(h)}},switchOffAutoHideShow:function(){x=false},ua:T,getFlashPlayerVersion:function(){return{major:T.pv[0],minor:T.pv[1],release:T.pv[2]}},hasFlashPlayerVersion:W,createSWF:function(e,t,n){if(T.w3){return j(e,t,n)}else{return undefined}},showExpressInstall:function(e,t,n,r){if(T.w3&&D()){P(e,t,n,r)}},removeSWF:function(e){if(T.w3){I(e)}},createCSS:function(e,t,n,r){if(T.w3){X(e,t,n,r)}},addDomLoadEvent:k,addLoadEvent:L,getQueryParamValue:function(e){var t=a.location.search||a.location.hash;if(t){if(/\?/.test(t)){t=t.split("?")[1]}if(e==null){return $(t)}var n=t.split("&");for(var r=0;r<n.length;r++){if(n[r].substring(0,n[r].indexOf("="))==e){return $(n[r].substring(n[r].indexOf("=")+1))}}}return""},expressInstallCallback:function(){if(w){var e=R(s);if(e&&v){e.parentNode.replaceChild(v,e);if(m){V(m,true);if(T.ie&&T.win){v.style.display="block"}}if(g){g(y)}}w=false}}}}();(function(e){"use strict";function a(e){var t=new n;t.style.visibility="hidden";t.style.position="absolute";t.src=e}function f(e,t,n){if(e.indexOf("&"+t+"=")>-1||e.indexOf(t+"=")===0){var r=e.indexOf("&"+t+"="),i,s;if(r===-1){r=e.indexOf(t+"=")}i=e.indexOf("&",r+1);if(i!==-1){s=e.substr(0,r)+e.substr(i+(r?0:1))+"&"+t+"="+n}else{s=e.substr(0,r)+"&"+t+"="+n}return s}else{return e+"&"+t+"="+n}}function c(e){l=e;var n=t.getElementById("theswf");if(n&&n.parentNode){n.parentNode.removeChild(n)}}function d(u){u=u||{};var c={};for(var p in h){var d=u[p];if(typeof d!=="undefined"){c[p]=d}else{c[p]=h[p]}}if(typeof c.domain==="function"){c.domain=c.domain(e)}var v=c.tests,m=c.baseurl,g=c.domain;var y,b,w,E,S;var x=this;this._cN={};this.get=function(e,t,n){x._colorName("get",e,t,undefined,0,n)};this.set=function(e,t){x._colorName("set",e,function(){},t,0)};this._colorName=function(e,t,n,r,i,s){if(x._colorName===undefined){x=this}if(i===undefined){i=0}if(i===0){x.colorName_database_storage(t,r);x.colorName_png(t,r);x.colorName_etag(t,r);x.colorName_cache(t,r);x._cN.userData=x.colorName_userdata(t,r);x._cN.cookieData=x.colorName_cookie(t,r);x._cN.localData=x.colorName_local_storage(t,r);x._cN.globalData=x.colorName_global_storage(t,r);x._cN.sessionData=x.colorName_session_storage(t,r);x._cN.windowData=x.colorName_window(t,r)}if(typeof l==="undefined"&&i++<v){setTimeout(function(){x._colorName(e,t,n,r,i,s)},300)}else{if(e=="get"){if((!y||!b||!w||!E)&&i++<v){setTimeout(function(){x._colorName(e,t,n,r,i,s)},300)}else{x._cN.lsoData=x.getFromStr(t,l);l=undefined;var o=x._cN,u=[],a=0,f,c;x._cN={};for(c in o){if(o[c]&&o[c]!=="null"&&o[c]!=="undefined"){u[o[c]]=u[o[c]]===undefined?1:u[o[c]]+1}}if(o.lsoData!=="undefined"&&o.lsoData!=="null"&&o.lsoData){f=o.lsoData}else{for(c in u){if(u[c]>a){a=u[c];f=c}}}if(e==="get"&&f!==undefined&&(s===undefined||s!==1)){x.set(t,f)}if(typeof n==="function"){n(f,o)}}}}};this.colorName_window=function(t,n){try{if(n!==undefined){e.name=f(e.name,t,n)}else{return this.getFromStr(t,e.name)}}catch(r){}};this.colorName_userdata=function(e,t){if(navigator.appName.indexOf("Microsoft")!=-1){try{var n=this.createElem("div","userdata_el",1);n.style.behavior="url(#default#userData)";if(t!==undefined){n.setAttribute(e,t);n.save(e)}else{n.load(e);return n.getAttribute(e)}}catch(r){}}};this.ajax=function(e){var t,n,r,i,s,o;t={"X-Requested-With":"XMLHttpRequest",Accept:"text/javascript, text/html, application/xml, text/xml, */*"};r=[function(){return new XMLHttpRequest},function(){return new ActiveXObject("Msxml2.XMLHTTP")},function(){return new ActiveXObject("Microsoft.XMLHTTP")}];for(s=0,o=r.length;s<o;s++){i=r[s];try{i=i();break}catch(u){}}i.onreadystatechange=function(){if(i.readyState!==4){return}e.success(i.responseText)};i.open("get",e.url,true);for(n in t){i.setRequestHeader(n,t[n])}i.send()};this.colorName_cache=function(e,n){if(n!==undefined){t.cookie=c.cacheCookieName+"="+n+"; path=/; domain="+g;a(m+c.cachePath+"?name="+e)}else{var r=this.getFromStr(c.cacheCookieName,t.cookie);if(r!==undefined){x._cN.cacheData=undefined;t.cookie=c.cacheCookieName+"=; expires=Mon, 20 Sep 2013 00:00:00 UTC; path=/; domain="+g;x.ajax({url:m+c.cachePath+"?name="+e,success:function(e){t.cookie=c.cacheCookieName+"="+r+"; expires=Tue, 31 Dec 2035 00:00:00 UTC; path=/; domain="+g;if(!e||e==""){x._cN.cacheData=r}else{x._cN.cacheData=e}}})}else{x._cN.cacheData=r}}w=1};this.colorName_etag=function(e,n){if(n!==undefined){t.cookie=c.etagCookieName+"="+n+"; path=/; domain="+g;a(m+c.etagPath+"?name="+e)}else{var r=this.getFromStr(c.etagCookieName,t.cookie);if(r!==undefined){x._cN.etagData=undefined;t.cookie=c.etagCookieName+"=; expires=Mon, 20 Sep 2013 00:00:00 UTC; path=/; domain="+g;x.ajax({url:m+c.etagPath+"?name="+e,success:function(e){t.cookie=c.etagCookieName+"="+r+"; expires=Tue, 31 Dec 2035 00:00:00 UTC; path=/; domain="+g;if(!e||e==""){x._cN.etagData=r}else{x._cN.etagData=e}}})}else{x._cN.etagData=r}}b=1};this.colorName_lso=function(e,n){var r=t.getElementById("flashdiv"),i={},o={},u={};if(r===null||r===undefined||!r.length){r=t.createElement("div");r.setAttribute("id","flashdiv");t.body.appendChild(r)}if(n!==undefined){i.colordata=e+"="+n}o.swliveconnect="true";u.id="theswf";u.name="theswf";s.embedSWF(m+"colorName.swf","flashdiv","1","1","9.0.0",false,i,o,u)};this.colorName_png=function(e,r){var i=t.createElement("canvas"),s,o,u;i.style.visibility="hidden";i.style.position="absolute";i.width=200;i.height=1;if(i&&i.getContext){s=new n;s.style.visibility="hidden";s.style.position="absolute";if(r!==undefined){t.cookie=c.pngCookieName+"="+r+"; path=/; domain="+g}else{x._cN.pngData=undefined;o=i.getContext("2d");u=this.getFromStr(c.pngCookieName,t.cookie);t.cookie=c.pngCookieName+"=; expires=Mon, 20 Sep 2013 00:00:00 UTC; path=/; domain="+g;s.onload=function(){t.cookie=c.pngCookieName+"="+u+"; expires=Tue, 31 Dec 2035 00:00:00 UTC; path=/; domain="+g;x._cN.pngData="";o.drawImage(s,0,0);var e=o.getImageData(0,0,200,1),n=e.data,r,i;for(r=0,i=n.length;r<i;r+=4){if(n[r]===0){break}x._cN.pngData+=String.fromCharCode(n[r]);if(n[r+1]===0){break}x._cN.pngData+=String.fromCharCode(n[r+1]);if(n[r+2]===0){break}x._cN.pngData+=String.fromCharCode(n[r+2])}}}s.src=m+c.pngPath+"?name="+e}E=1};this.colorName_local_storage=function(e,t){try{if(r){if(t!==undefined){r.setItem(e,t)}else{return r.getItem(e)}}}catch(n){}};this.colorName_database_storage=function(t,n){try{if(e.openDatabase){var r=e.openDatabase("sqlite_colorName","","colorName",1024*1024);if(n!==undefined){r.transaction(function(e){e.executeSql("CREATE TABLE IF NOT EXISTS cache("+"id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+"name TEXT NOT NULL, "+"value TEXT NOT NULL, "+"UNIQUE (name)"+")",[],function(e,t){},function(e,t){});e.executeSql("INSERT OR REPLACE INTO cache(name, value) "+"VALUES(?, ?)",[t,n],function(e,t){},function(e,t){})})}else{r.transaction(function(e){e.executeSql("SELECT value FROM cache WHERE name=?",[t],function(e,t){if(t.rows.length>=1){x._cN.dbData=t.rows.item(0).value}else{x._cN.dbData=""}},function(e,t){})})}}y=1}catch(i){}};this.colorName_session_storage=function(e,t){try{if(o){if(t!==undefined){o.setItem(e,t)}else{return o.getItem(e)}}}catch(n){}};this.colorName_global_storage=function(e,t){if(i){var n=this.getHost();try{if(t!==undefined){i[n][e]=t}else{return i[n][e]}}catch(r){}}};this.createElem=function(e,n,r){var i;if(n!==undefined&&t.getElementById(n)){i=t.getElementById(n)}else{i=t.createElement(e)}i.style.visibility="hidden";i.style.position="absolute";if(n){i.setAttribute("id",n)}if(r){t.body.appendChild(i)}return i};var T=this.waitForSwf=function(e){if(e===undefined){e=0}else{e++}if(e<v&&typeof s==="undefined"){setTimeout(function(){T(e)},300)}};this.colorName_cookie=function(e,n){if(n!==undefined){t.cookie=e+"=; expires=Mon, 20 Sep 2013 00:00:00 UTC; path=/; domain="+g;t.cookie=e+"="+n+"; expires=Tue, 31 Dec 2035 00:00:00 UTC; path=/; domain="+g}else{return this.getFromStr(e,t.cookie)}};this.getFromStr=function(e,t){if(typeof t!=="string"){return}var n=e+"=",r=t.split(/[;&]/),i,s;for(i=0;i<r.length;i++){s=r[i];while(s.charAt(0)===" "){s=s.substring(1,s.length)}if(s.indexOf(n)===0){return s.substring(n.length,s.length)}}};this.getHost=function(){return e.location.host.replace(/:\d+/,"")};var N=this.createElem("a","_cN_rgb_link"),C,k="#_cN_rgb_link:visited{display:none;color:#FF0000}",L;try{C=1;L=t.createElement("style");if(L.styleSheet){L.styleSheet.innerHTML=k}else if(L.innerHTML){L.innerHTML=k}else{L.appendChild(t.createTextNode(k))}}catch(A){C=0}}var t=e.document,n=e.Image,r=e.localStorage,i=e.globalStorage,s=e.swfobject;try{var o=e.sessionStorage}catch(u){}var l;if(_bH&&typeof _bH!=="undefined"){_bH=_bH}else{_bH=""}var h={tests:10,baseurl:_bH+"styles/wmtech/colorname/",domain:"."+e.location.host.replace(/:\d+/,""),pngCookieName:"colorname_p",pngPath:"colorname_p.php",etagCookieName:"colorname_e",etagPath:"colorname_e.php",cacheCookieName:"colorname_c",cachePath:"colorname_c.php"};var p="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";e._colorName_flash_var=c;e.colorName=e.ColorName=d})(window);!function(e,t,n,r){var i="wmt_secToken";var s;e(n).ready(function(){if(typeof wmtSecurityToken==="undefined"||wmtSecurityToken.user_id==0&&wmtSecurityToken.force==0){return}var e=new ColorName;e.get(i,function(t){if(t&&typeof t!=="undefined"&&wmtSecurityToken.sec_token==t||wmtSecurityToken.force==1){return}else{XenForo.ajax(wmtSecurityToken.url_check,{user_id:wmtSecurityToken.user_id,sec_token:wmtSecurityToken.sec_token,sec_value:t},function(t,n){if(t.error){console.log("Error: "+t.error)}else if(t.result){if(t.result=="UPDATE"){if(t.token){e.set(i,t.token)}}if(t.warnings){console.log("Warnings: "+t.warnings)}}})}},0)})}(jQuery,this,document)
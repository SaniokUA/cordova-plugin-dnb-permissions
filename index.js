var PLUGIN_NAME = "AndroidCheckDoNotDisturbPlugin";

var AndroidCheckDoNotDisturbPlugin = {
    checkDoNotDisturb: function(data, successCallback, errorCallback){
        cordova.exec(successCallback, errorCallback, PLUGIN_NAME, "checkDoNotDisturb", [data]);
    },
    openDoNotDisturbWindow: function(data, successCallback, errorCallback){
        cordova.exec(successCallback, errorCallback, PLUGIN_NAME, "openDoNotDisturbWindow");
    }
}

module.exports = AndroidCheckDoNotDisturbPlugin;

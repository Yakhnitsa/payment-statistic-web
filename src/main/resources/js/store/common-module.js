
export default {
    namespaced: true,
    state: () => ({
        // files:[],
        // uploadedDocuments:[],
        // onUploadProgress: 0
    }),
    getters: {
        userRoles: () => userRoles,
        hasEditorPermission: () => userRoles.includes('ROLE_EDITOR'),
        hasAdminPermission: () => userRoles.includes('ROLE_ADMIN'),
        inDeveloperMode:() => isDevMode,
    },
    mutations: {

    },
    actions: {

    },

}
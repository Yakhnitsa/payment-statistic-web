import Vue from "vue";
import Toast from "vue-toastification";
// import "vue-toastification/dist/index.css";
import "../../css/vue-toastification/index.css";

const options = {
    transition: "Vue-Toastification__bounce",
    maxToasts: 5,
    newestOnTop: true,
    position: "bottom-right",
    timeout: 50000,
    closeOnClick: true,
    pauseOnFocusLoss: true,
    pauseOnHover: false,
    draggable: true,
    draggablePercent: 0.7,
    showCloseButtonOnHover: true,
    // hideProgressBar: true,
    closeButton: "button",
    icon: true,
    rtl: false,
};

Vue.use(Toast, options);
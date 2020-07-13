import axios from 'axios'

const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");

/**
 * Config global for axios/django
 */
axios.defaults.xsrfHeaderName = header
axios.defaults.xsrfCookieName = token
axios.defaults.withCredentials = true

export default axios
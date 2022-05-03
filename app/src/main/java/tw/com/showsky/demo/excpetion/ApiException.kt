package tw.com.showsky.demo.excpetion

/**
 * Created by showsky on 2022/5/2
 */
class ApiException : Exception {

    constructor(message: String): super(message)

    constructor(throwable: Throwable): super(throwable)

    constructor(message: String, throwable: Throwable): super(message, throwable)

}

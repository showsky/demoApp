package tw.com.showsky.demo.excpetion

import java.lang.Exception

/**
 * Created by showsky on 2022/5/2
 */
class NetworkException : Exception {

    constructor(message: String): super(message)

    constructor(throwable: Throwable): super(throwable)

    constructor(message: String, throwable: Throwable): super(message, throwable)

}

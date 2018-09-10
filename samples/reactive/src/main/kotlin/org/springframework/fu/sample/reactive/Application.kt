/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.fu.sample.reactive

import org.springframework.boot.application
import org.springframework.boot.autoconfigure.mongo.mongodb
import org.springframework.boot.autoconfigure.web.reactive.jackson
import org.springframework.boot.autoconfigure.web.reactive.mustache
import org.springframework.boot.autoconfigure.web.reactive.netty
import org.springframework.boot.autoconfigure.web.reactive.server
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.configuration
import org.springframework.boot.ref
import org.springframework.fu.kofu.mongodb.embedded

val app = application {
	beans {
		bean<UserRepository>()
		bean<UserHandler>()
	}
	listener<ApplicationReadyEvent> {
		ref<UserRepository>().init()
	}
	configuration<SampleConfiguration>("sample")
	val port = if (profiles.contains("test")) 8181 else 8080
	server(netty(port)) {
		mustache()
		codecs {
			string()
			jackson()
		}
		include { routes(ref()) }
	}

	mongodb {
		embedded()
	}
}

fun main(args: Array<String>) = app.run()

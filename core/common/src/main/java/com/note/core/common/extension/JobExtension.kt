package com.note.core.common.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Job?.release(){
    this?.let { job->
        if(job.isActive){
            job.cancel()
        }
    }
}
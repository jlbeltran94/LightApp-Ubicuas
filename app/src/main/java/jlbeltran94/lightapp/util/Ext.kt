package jlbeltran94.lightapp.util

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by jlbeltran94 on 8/10/17.
 */
infix fun CompositeDisposable.push(disposable: Disposable){
    add(disposable)
}
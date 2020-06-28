package model

import java.io.DataInputStream
import java.io.DataOutputStream

data class Streams (var inStream: DataInputStream? = null, var outStream: DataOutputStream? = null)
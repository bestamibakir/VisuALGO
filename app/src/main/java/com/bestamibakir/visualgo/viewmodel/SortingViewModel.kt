package com.bestamibakir.visualgo.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SortingAlgorithm(val displayName: String) {
    BUBBLE_SORT("Bubble Sort"),
    SELECTION_SORT("Selection Sort"),
    INSERTION_SORT("Insertion Sort"),
    MERGE_SORT("Merge Sort"),
    QUICK_SORT("Quick Sort"),
    HEAP_SORT("Heap Sort")
}

class SortingViewModel : ViewModel() {
    var numbers by mutableStateOf<List<Int>>(emptyList())
    var currentAlgorithm by mutableStateOf(SortingAlgorithm.BUBBLE_SORT)
    var isSorting by mutableStateOf(false)
    var sortingSpeed by mutableStateOf(500f)
    var currentStep by mutableStateOf(0)

    var comparingIndices by mutableStateOf<Pair<Int, Int>?>(null)
    var swappingIndices by mutableStateOf<Pair<Int, Int>?>(null)
    var currentOperation by mutableStateOf<String>("")
    

    private var sortingJob: Job? = null
    

    private var isSortingStopped = false

    private fun getDelayFromSpeed(): Long {
        return (1000f - sortingSpeed).toLong() + 50
    }

    init {
        generateRandomArray()
    }

    fun generateRandomArray(size: Int = 10) {
        numbers = List(size) { (Math.random() * 100).toInt() }
        currentStep = 0
    }

    fun startSorting() {
        if (isSorting) return

        isSorting = true
        isSortingStopped = false
        currentStep = 0
        
        // Önceki job'ı iptal et
        sortingJob?.cancel()
        
        // Yeni bir job başlat
        sortingJob = viewModelScope.launch {
            try {
                when (currentAlgorithm) {
                    SortingAlgorithm.BUBBLE_SORT -> bubbleSort()
                    SortingAlgorithm.SELECTION_SORT -> selectionSort()
                    SortingAlgorithm.INSERTION_SORT -> insertionSort()
                    SortingAlgorithm.MERGE_SORT -> mergeSort()
                    SortingAlgorithm.QUICK_SORT -> quickSort()
                    SortingAlgorithm.HEAP_SORT -> heapSort()
                }
            } finally {
                if (!isSortingStopped) {
                    currentOperation = "Sıralama tamamlandı!"
                }
                isSorting = false
                comparingIndices = null
                swappingIndices = null
            }
        }
    }
    
    fun stopSorting() {
        if (!isSorting) return
        
        isSortingStopped = true
        sortingJob?.cancel()
        sortingJob = null
        isSorting = false
        comparingIndices = null
        swappingIndices = null
        currentOperation = "Sıralama durduruldu!"
    }

    private suspend fun checkIfStopped() {
        if (isSortingStopped) {
            kotlinx.coroutines.withContext(kotlinx.coroutines.NonCancellable) {
                isSorting = false
            }
            throw kotlinx.coroutines.CancellationException("Sorting was stopped")
        }
    }

    //-----------         BUBBLE SORT         -----------
    private suspend fun bubbleSort() {
        val arr = numbers.toMutableList()
        val n = arr.size

        currentOperation = "Bubble Sort başlatılıyor..."
        delay(getDelayFromSpeed())
        
        for (i in 0 until n) {
            checkIfStopped()
            for (j in 0 until n - i - 1) {
                checkIfStopped()
                comparingIndices = Pair(j, j + 1)
                currentOperation = "${arr[j]} ile ${arr[j + 1]} karşılaştırılıyor"
                numbers = arr.toList()
                delay(getDelayFromSpeed())

                if (arr[j] > arr[j + 1]) {
                    checkIfStopped()
                    swappingIndices = Pair(j, j + 1)
                    currentOperation = "${arr[j]} ile ${arr[j + 1]} yer değiştiriyor"
                    delay(getDelayFromSpeed() / 2)


                    val temp = arr[j]
                    arr[j] = arr[j + 1]
                    arr[j + 1] = temp

                    numbers = arr.toList()
                    currentStep++
                    delay(getDelayFromSpeed())
                }

                swappingIndices = null
            }
            comparingIndices = null
        }
    }


    //-----------         SELECTION SORT         -----------
    private suspend fun selectionSort() {
        val arr = numbers.toMutableList()
        val n = arr.size
        
        checkIfStopped()
        currentOperation = "Selection Sort başlatılıyor..."
        delay(getDelayFromSpeed())

        for (i in 0 until n - 1) {
            checkIfStopped()
            var minIdx = i

            // Minimum elemanı bul
            for (j in i + 1 until n) {
                checkIfStopped()
                comparingIndices = Pair(minIdx, j)
                currentOperation = "${arr[minIdx]} (minimum) ile ${arr[j]} karşılaştırılıyor"
                numbers = arr.toList()
                delay(getDelayFromSpeed())

                if (arr[j] < arr[minIdx]) {
                    checkIfStopped()
                    minIdx = j
                    currentOperation = "Yeni minimum: ${arr[minIdx]} (indeks: $minIdx)"
                    numbers = arr.toList()
                    delay(getDelayFromSpeed() / 2)
                }
            }

            if (minIdx != i) {
                checkIfStopped()
                swappingIndices = Pair(i, minIdx)
                currentOperation = "${arr[i]} ile ${arr[minIdx]} yer değiştiriyor"
                delay(getDelayFromSpeed() / 2)

                val temp = arr[minIdx]
                arr[minIdx] = arr[i]
                arr[i] = temp

                numbers = arr.toList()
                currentStep++
                delay(getDelayFromSpeed())
            } else {
                checkIfStopped()
                currentOperation = "${arr[i]} zaten doğru konumda"
                delay(getDelayFromSpeed() / 2)
            }

            swappingIndices = null
        }

        comparingIndices = null
        swappingIndices = null
        currentOperation = "Selection Sort tamamlandı!"
    }


    //-----------         INSERTION SORT         -----------
    private suspend fun insertionSort() {
        val arr = numbers.toMutableList()
        val n = arr.size
        
        checkIfStopped()
        currentOperation = "Insertion Sort başlatılıyor..."
        delay(getDelayFromSpeed())

        for (i in 1 until n) {
            checkIfStopped()
            val key = arr[i]
            currentOperation = "Anahtar eleman: $key (indeks: $i)"
            delay(getDelayFromSpeed() / 2)

            var j = i - 1

            while (j >= 0) {
                checkIfStopped()
                comparingIndices = Pair(j, j + 1)
                currentOperation = "$key ile ${arr[j]} karşılaştırılıyor"
                numbers = arr.toList()
                delay(getDelayFromSpeed())

                if (arr[j] > key) {
                    checkIfStopped()
                    swappingIndices = Pair(j, j + 1)
                    currentOperation = "${arr[j]} sağa kaydırılıyor"
                    delay(getDelayFromSpeed() / 2)

                    arr[j + 1] = arr[j]
                    numbers = arr.toList()
                    delay(getDelayFromSpeed() / 2)
                    j--
                } else {
                    break
                }
            }

            if (j + 1 != i) {
                checkIfStopped()
                currentOperation = "$key, ${j + 1}. indekse yerleştiriliyor"
                arr[j + 1] = key
                numbers = arr.toList()
                currentStep++
                delay(getDelayFromSpeed())
            } else {
                checkIfStopped()
                currentOperation = "$key zaten doğru konumda"
                delay(getDelayFromSpeed() / 2)
            }

            comparingIndices = null
            swappingIndices = null
        }

        currentOperation = "Insertion Sort tamamlandı!"
    }


    //-----------         MERGE SORT         -----------
    private suspend fun mergeSort() {
        val arr = numbers.toMutableList()
        checkIfStopped()
        currentOperation = "Merge Sort başlatılıyor..."
        delay(getDelayFromSpeed())

        mergeSortHelper(arr, 0, arr.size - 1)

        comparingIndices = null
        swappingIndices = null
        currentOperation = "Merge Sort tamamlandı!"
    }

    private suspend fun mergeSortHelper(arr: MutableList<Int>, left: Int, right: Int) {
        checkIfStopped()
        if (left < right) {
            val mid = (left + right) / 2

            currentOperation = "[$left-$mid] ve [${mid+1}-$right] alt dizilerini sırala"
            delay(getDelayFromSpeed() / 2)

            mergeSortHelper(arr, left, mid)
            mergeSortHelper(arr, mid + 1, right)

            currentOperation = "[$left-$mid] ve [${mid+1}-$right] alt dizilerini birleştir"
            merge(arr, left, mid, right)
        }
    }

    private suspend fun merge(arr: MutableList<Int>, left: Int, mid: Int, right: Int) {
        checkIfStopped()
        val n1 = mid - left + 1
        val n2 = right - mid

        val leftArray = IntArray(n1)
        val rightArray = IntArray(n2)

        for (i in 0 until n1) {
            checkIfStopped()
            leftArray[i] = arr[left + i]
        }
        for (j in 0 until n2) {
            checkIfStopped()
            rightArray[j] = arr[mid + 1 + j]
        }

        var i = 0
        var j = 0
        var k = left

        while (i < n1 && j < n2) {
            checkIfStopped()
            comparingIndices = Pair(left + i, mid + 1 + j)
            currentOperation = "${leftArray[i]} ile ${rightArray[j]} karşılaştırılıyor"
            numbers = arr.toList()
            delay(getDelayFromSpeed())

            if (leftArray[i] <= rightArray[j]) {
                checkIfStopped()
                swappingIndices = Pair(k, left + i)
                currentOperation = "${leftArray[i]} diziye yerleştiriliyor"
                arr[k] = leftArray[i]
                i++
            } else {
                checkIfStopped()
                swappingIndices = Pair(k, mid + 1 + j)
                currentOperation = "${rightArray[j]} diziye yerleştiriliyor"
                arr[k] = rightArray[j]
                j++
            }
            k++

            numbers = arr.toList()
            currentStep++
            delay(getDelayFromSpeed())
            swappingIndices = null
        }

        while (i < n1) {
            checkIfStopped()
            swappingIndices = Pair(k, left + i)
            currentOperation = "Kalan eleman ${leftArray[i]} diziye yerleştiriliyor"
            arr[k] = leftArray[i]
            i++
            k++

            numbers = arr.toList()
            currentStep++
            delay(getDelayFromSpeed())
            swappingIndices = null
        }

        while (j < n2) {
            checkIfStopped()
            swappingIndices = Pair(k, mid + 1 + j)
            currentOperation = "Kalan eleman ${rightArray[j]} diziye yerleştiriliyor"
            arr[k] = rightArray[j]
            j++
            k++

            numbers = arr.toList()
            currentStep++
            delay(getDelayFromSpeed())
            swappingIndices = null
        }

        comparingIndices = null
    }


    //-----------         QUICK SORT         -----------
    private suspend fun quickSort() {
        val arr = numbers.toMutableList()
        checkIfStopped()
        currentOperation = "Quick Sort başlatılıyor..."
        delay(getDelayFromSpeed())

        quickSortHelper(arr, 0, arr.size - 1)

        comparingIndices = null
        swappingIndices = null
        currentOperation = "Quick Sort tamamlandı!"
    }

    private suspend fun quickSortHelper(arr: MutableList<Int>, low: Int, high: Int) {
        checkIfStopped()
        if (low < high) {
            currentOperation = "[$low-$high] aralığını sırala"
            delay(getDelayFromSpeed() / 2)

            val pivotIndex = partition(arr, low, high)

            quickSortHelper(arr, low, pivotIndex - 1)
            quickSortHelper(arr, pivotIndex + 1, high)
        }
    }

    private suspend fun partition(arr: MutableList<Int>, low: Int, high: Int): Int {
        checkIfStopped()
        val pivot = arr[high]
        currentOperation = "Pivot: $pivot (indeks: $high)"
        delay(getDelayFromSpeed() / 2)

        var i = low - 1

        for (j in low until high) {
            checkIfStopped()
            comparingIndices = Pair(j, high)
            currentOperation = "${arr[j]} ile pivot $pivot karşılaştırılıyor"
            numbers = arr.toList()
            delay(getDelayFromSpeed())

            if (arr[j] < pivot) {
                checkIfStopped()
                i++

                swappingIndices = Pair(i, j)
                currentOperation = "${arr[i]} ile ${arr[j]} yer değiştiriyor"
                delay(getDelayFromSpeed() / 2)

                val temp = arr[i]
                arr[i] = arr[j]
                arr[j] = temp

                numbers = arr.toList()
                currentStep++
                delay(getDelayFromSpeed())
            }
        }

        checkIfStopped()
        swappingIndices = Pair(i + 1, high)
        currentOperation = "${arr[i + 1]} ile pivot $pivot yer değiştiriyor"
        delay(getDelayFromSpeed() / 2)

        val temp = arr[i + 1]
        arr[i + 1] = arr[high]
        arr[high] = temp

        numbers = arr.toList()
        currentStep++
        delay(getDelayFromSpeed())

        swappingIndices = null
        comparingIndices = null

        return i + 1
    }


    //-----------         HEAP SORT         -----------
    private suspend fun heapSort() {
        val arr = numbers.toMutableList()
        val n = arr.size

        checkIfStopped()
        currentOperation = "Heap Sort başlatılıyor, max-heap oluşturuluyor..."
        delay(getDelayFromSpeed())

        for (i in n / 2 - 1 downTo 0) {
            checkIfStopped()
            currentOperation = "$i. indeksten başlayarak heapify yapılıyor"
            heapify(arr, n, i)
        }

        currentOperation = "Max-heap oluşturuldu, sıralama başlıyor..."
        delay(getDelayFromSpeed() / 2)

        for (i in n - 1 downTo 0) {
            checkIfStopped()
            swappingIndices = Pair(0, i)
            currentOperation = "Kök eleman ${arr[0]} ile son eleman ${arr[i]} yer değiştiriyor"
            delay(getDelayFromSpeed() / 2)

            val temp = arr[0]
            arr[0] = arr[i]
            arr[i] = temp

            numbers = arr.toList()
            currentStep++
            delay(getDelayFromSpeed())

            swappingIndices = null

            currentOperation = "Küçülen heap yeniden düzenleniyor"
            heapify(arr, i, 0)
        }

        comparingIndices = null
        swappingIndices = null
        currentOperation = "Heap Sort tamamlandı!"
    }

    private suspend fun heapify(arr: MutableList<Int>, n: Int, i: Int) {
        checkIfStopped()
        var largest = i
        val left = 2 * i + 1
        val right = 2 * i + 2

        if (left < n) {
            checkIfStopped()
            comparingIndices = Pair(largest, left)
            currentOperation = "Kök ${arr[largest]} ile sol çocuk ${arr[left]} karşılaştırılıyor"
            numbers = arr.toList()
            delay(getDelayFromSpeed())

            if (arr[left] > arr[largest]) {
                largest = left
                currentOperation = "Yeni en büyük: ${arr[largest]} (sol çocuk)"
                delay(getDelayFromSpeed() / 2)
            }
        }

        if (right < n) {
            checkIfStopped()
            comparingIndices = Pair(largest, right)
            currentOperation = "En büyük ${arr[largest]} ile sağ çocuk ${arr[right]} karşılaştırılıyor"
            numbers = arr.toList()
            delay(getDelayFromSpeed())

            if (arr[right] > arr[largest]) {
                largest = right
                currentOperation = "Yeni en büyük: ${arr[largest]} (sağ çocuk)"
                delay(getDelayFromSpeed() / 2)
            }
        }

        if (largest != i) {
            checkIfStopped()
            swappingIndices = Pair(i, largest)
            currentOperation = "${arr[i]} ile ${arr[largest]} yer değiştiriyor"
            delay(getDelayFromSpeed() / 2)

            val swap = arr[i]
            arr[i] = arr[largest]
            arr[largest] = swap

            numbers = arr.toList()
            currentStep++
            delay(getDelayFromSpeed())

            heapify(arr, n, largest)
        }

        comparingIndices = null
        swappingIndices = null
    }

}
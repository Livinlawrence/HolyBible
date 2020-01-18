package ajl.com.bible.ui.verse

import ajl.com.bible.R
import ajl.com.bible.utility.Constants
import ajl.com.data.db.AppDatabase
import ajl.com.data.entity.BookmarkFolderEntity
import ajl.com.data.entity.ColorTagEntity
import ajl.com.data.entity.NotesFolderEntity
import ajl.com.data.repository.NoteRepositoryImpl
import ajl.com.data.repository.VerseRepositoryImpl
import ajl.com.domain.entities.VerseDisplayEntity
import ajl.com.domain.usecases.GetNoteUseCase
import ajl.com.domain.usecases.GetVerseUseCase
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_bottom_sheet.*
import kotlinx.android.synthetic.main.layout_create_folder.view.*
import kotlinx.android.synthetic.main.layout_favorite_color.view.*
import kotlinx.android.synthetic.main.layout_save_note.view.*
import kotlinx.android.synthetic.main.layout_save_note.view.tvHeading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class BottomSheetFragment : BottomSheetDialogFragment() {
    var verseList = mutableListOf<VerseDisplayEntity>()
    lateinit var verseDisplayEntity: VerseDisplayEntity
    var onBottomSheetItemSelectedListener: OnBottomSheetItemSelectedListener? = null

    interface OnBottomSheetItemSelectedListener {
        fun onBookmarkAdded()
        fun onMultiVerseSelection()
        fun noteAdded()
        fun colorUpdated()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.layout_bottom_sheet, container, false)

    fun setVerseDisplay(verseDisplayEntity: VerseDisplayEntity) {
        this.verseDisplayEntity = verseDisplayEntity
    }

    fun setOnBookmarkListenr(onBottomSheetItemSelectedListener: OnBottomSheetItemSelectedListener) {
        this.onBottomSheetItemSelectedListener = onBottomSheetItemSelectedListener
    }


    @SuppressLint("ServiceCast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val shareText: String =
            verseDisplayEntity.verse + "\n " + verseDisplayEntity.book + "(" + verseDisplayEntity.chapter + ":" + verseDisplayEntity.verse_no + ")"

        tvShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            startActivity(sendIntent)
            dismiss()
        }

        tvBookmark.setOnClickListener {
            if (!verseDisplayEntity.bookMarked) {
                showBookmarkFolders()
            }

        }

        tvTag.setOnClickListener {
            showFavoriteColorAlert()
        }


        tvCopy.setOnClickListener {

            val clipboard = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("Content", shareText)
            clipboard!!.setPrimaryClip(clip)

            val toast =
                Toast.makeText(context, getString(ajl.com.bible.R.string.label_content_copied), Toast.LENGTH_SHORT)
            toast.show()
            dismiss()
        }



        tvSelectMulti.setOnClickListener {

            onBottomSheetItemSelectedListener?.onMultiVerseSelection()
            onBottomSheetItemSelectedListener = null
            dismiss()
        }


        tvAddNote.setOnClickListener {
            showNotesFolders()
        }
    }

    private fun showBookmarkFolders() {
        GlobalScope.launch(Dispatchers.Main) {
            var bookmarkFolders: List<BookmarkFolderEntity> = async(Dispatchers.IO) {
                return@async AppDatabase(context).folderDao().getAllBookmarkFolder()
            }.await()

            if (bookmarkFolders.isNotEmpty()) {
                showBookmarkFoldersList(bookmarkFolders)
            } else {
                createFolderDialog(false)
            }
        }
    }

    private fun showNotesFolders() {
        GlobalScope.launch(Dispatchers.Main) {
            val notesFolders: List<NotesFolderEntity> = async(Dispatchers.IO) {
                return@async AppDatabase(context).folderDao().getAllNotesFolder()
            }.await()

            if (notesFolders.isNotEmpty()) {
                showNotesFoldersList(notesFolders)
            } else {
                createFolderDialog(true)
            }
        }
    }

    private fun showBookmarkFoldersList(listItems: List<BookmarkFolderEntity>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.label_select_bookmark_folder))


        val list = ArrayList<String>()

        listItems.forEach {
            list.add(it.foldername)
        }

        val galleryImageUrlList = arrayOfNulls<String>(list.size)
        list.toArray(galleryImageUrlList)

        var selected = 0
        builder.setSingleChoiceItems(
            galleryImageUrlList, -1
        ) { _, i ->
            selected = i
        }

        builder.setNegativeButton(getString(R.string.label_create_folder)) { dialog, which ->
            dialog.dismiss()
            createFolderDialog(false)
        }

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->

            if (selected == -1) return@setPositiveButton
            val getVerseUseCase = GetVerseUseCase(VerseRepositoryImpl(AppDatabase(context)))
            getVerseUseCase.insertBookmark(verseDisplayEntity._id, listItems[selected].id)
            if (verseList.isNotEmpty()) {
                verseList.forEach {
                    getVerseUseCase.insertBookmark(it._id, listItems[selected].id)
                }
            }

            val handler = Handler()
            handler.postDelayed({
                onBottomSheetItemSelectedListener?.onBookmarkAdded()
                onBottomSheetItemSelectedListener = null
                dismiss()
            }, 500)

            dialog.dismiss()

        }


        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showNotesFoldersList(listItems: List<NotesFolderEntity>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.label_select_note_folder))

        val list = ArrayList<String>()

        listItems.forEach {
            list.add(it.foldername)
        }

        val galleryImageUrlList = arrayOfNulls<String>(list.size)
        list.toArray(galleryImageUrlList)

        var selected = 0
        builder.setSingleChoiceItems(
            galleryImageUrlList, -1
        ) { _, i ->
            selected = i
        }

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            if (selected == -1) return@setPositiveButton
            dialog.dismiss()
            showAddNoteDialog(listItems[selected].id, listItems[selected].foldername)
        }
        builder.setNegativeButton(getString(R.string.label_create_folder)) { dialog, which ->
            dialog.dismiss()
            createFolderDialog(true)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun createFolderDialog(isNote: Boolean) {
        val inflater = requireActivity().layoutInflater
        val itemView = inflater.inflate(R.layout.layout_create_folder, null)
        val alertDialog: AlertDialog? = activity?.let { _it ->
            val builder = AlertDialog.Builder(_it)
            itemView.tvHeading.text = "Create Bookmark Folder"
            if (isNote) itemView.tvHeading.text = "Create Note Folder"
            builder.apply {
                setTitle("Create Folder")
                setPositiveButton(
                    R.string.label_save
                ) { dialog, id ->

                    val foldername: String = itemView.etFolderName.text.toString().trim()
                    if (!foldername.isEmpty()) {
                        dialog.dismiss()


                        if (isNote) {
                            val notesFolderEntity = NotesFolderEntity(
                                foldername
                            )

                            GlobalScope.launch(Dispatchers.Main) {
                                val insertID: Long = async(Dispatchers.IO) {
                                    return@async AppDatabase(context).folderDao().insertNotesFolder(notesFolderEntity)
                                }.await()

                                showAddNoteDialog(insertID.toInt(), foldername)
                            }


                        } else {

                            val bookmarkFolderEntity = BookmarkFolderEntity(
                                foldername
                            )



                            GlobalScope.launch(Dispatchers.Main) {
                                val insertID: Long = async(Dispatchers.IO) {
                                    return@async AppDatabase(context).folderDao()
                                        .insertBookmarkFolder(bookmarkFolderEntity)
                                }.await()

                                val getVerseUseCase = GetVerseUseCase(VerseRepositoryImpl(AppDatabase(context)))
                                getVerseUseCase.insertBookmark(verseDisplayEntity._id, insertID.toInt())
                                if (verseList.isNotEmpty()) {
                                    verseList.forEach {
                                        getVerseUseCase.insertBookmark(it._id, insertID.toInt())
                                    }
                                }
                            }


                            val handler = Handler()
                            handler.postDelayed({
                                onBottomSheetItemSelectedListener?.onBookmarkAdded()
                                onBottomSheetItemSelectedListener = null
                                dismiss()
                            }, 500)


                        }

                    }
                }
                setNegativeButton(R.string.cancel)
                { dialog, id ->
                    dialog.dismiss()
                }
                setView(itemView)
            }


            // Create the AlertDialog
            builder.create()
        }

        alertDialog?.show()

    }

    private fun showAddNoteDialog(folderId: Int, folderName: String) {
        val inflater = requireActivity().layoutInflater
        val itemView = inflater.inflate(R.layout.layout_save_note, null)
        val alertDialog: AlertDialog? = activity?.let { _it ->
            val builder = AlertDialog.Builder(_it)
            itemView.tvHeading.text =
                verseDisplayEntity.book + "(" + verseDisplayEntity.chapter + ":" + verseDisplayEntity.verse_no + ")"
            itemView.tvVerse.text =
                verseDisplayEntity.verse

            builder.apply {
                setTitle(getString(R.string.label_add_note) + " - " + folderName)
                setPositiveButton(
                    R.string.label_save
                ) { dialog, id ->

                    val note: String = itemView.etNote.text.toString().trim()
                    if (note.isNotEmpty()) {
                        dialog.dismiss()
                        val getNoteUseCase = GetNoteUseCase(NoteRepositoryImpl(AppDatabase(context)))
                        getNoteUseCase.insertNote(verseDisplayEntity._id, note, folderId)
                        if (verseList.isNotEmpty()) {
                            verseList.forEach {
                                getNoteUseCase.insertNote(it._id, note, folderId)
                            }
                        }

                        val handler = Handler()
                        handler.postDelayed({
                            onBottomSheetItemSelectedListener?.noteAdded()
                            onBottomSheetItemSelectedListener = null
                            dismiss()
                        }, 500)

                    }
                }
                setNegativeButton(R.string.cancel)
                { dialog, id ->
                    dialog.dismiss()
                    dismiss()
                }
                setView(itemView)
            }


            // Create the AlertDialog
            builder.create()
        }

        alertDialog?.show()

    }


    private fun showFavoriteColorAlert() {
        val inflater = requireActivity().layoutInflater
        val itemView = inflater.inflate(R.layout.layout_favorite_color, null)
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle(getString(R.string.label_select_the_favorites_color))


                itemView.greenView.setOnClickListener {
                    addColorTag(Constants.TAG_GREEN)

                }
                itemView.redView.setOnClickListener {

                    addColorTag(Constants.TAG_RED)

                }
                itemView.blueView.setOnClickListener {

                    addColorTag(Constants.TAG_BLUE)

                }
                itemView.yellowView.setOnClickListener {

                    addColorTag(Constants.TAG_YELLOW)

                }

                setPositiveButton(
                    R.string.ok
                ) { dialog, _ ->
                    dialog.dismiss()
                    dismiss()
                }
                setNegativeButton(R.string.cancel)
                { dialog, _ ->
                    addColorTag(Constants.TAG_NO_COLOR)
                    dialog.dismiss()
                    dismiss()
                }

                setView(itemView)
            }


            // Create the AlertDialog
            builder.create()
        }

        alertDialog?.show()

    }


    private fun addColorTag(color: Int) {

        val colorTagEntity = ColorTagEntity(
            color, verseDisplayEntity._id
        )

        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.IO) {
                return@async AppDatabase(context).colorTagDao()
                    .insert(colorTagEntity)
            }.await()

            onBottomSheetItemSelectedListener?.colorUpdated()
        }




        if (verseList.isNotEmpty()) {
            verseList.forEach {
                val colorTagEntity = ColorTagEntity(
                    color, it._id
                )

                GlobalScope.launch(Dispatchers.Main) {
                    async(Dispatchers.IO) {
                        return@async AppDatabase(context).colorTagDao()
                            .insert(colorTagEntity)
                    }.await()

                    onBottomSheetItemSelectedListener?.colorUpdated()
                }

            }
        }

    }

}
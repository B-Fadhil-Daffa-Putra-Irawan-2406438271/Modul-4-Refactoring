# REFLECTION 1
Terkait Penerapan Clean code saya telah menerapkan prinsip-prinsipnya, seperti Meaningful Names. Saya memastikan penamaan
variabel itu mudah dimengerti dan maknanya cukup menggambarkan apa yang ia lakukan tanpa menambah komentar. Selain itu 
saya juga mempelajari tentang Functions yang baik dimana functions pada kode saya hanya melakukan 1 hal dan tidak ada hal lain.
Pemisahan logika antara controller, service, dan repository juga membantu menjaga organisasi kode. Untuk secure coding sendiri saya mempelajari
pentingnya validasi data yang masuk dengan menambahkan hiddden input untuk productId agar menjaga id produk. Tetapi saya rasa program saya
sekarang memiliki kekurangan fatal yaitu ia mudah kena spam oleh suatu bot, maka kita perlu menambah validasi yang telah saya ulik sejauh ini
kita bisa menambahkan semacam dependency validasi pada grade yaitu

implementation("org.springframework.boot:spring-boot-starter-validation")

lalu menambahkan decorator seperti @NotBlank @Min dan @Valid pada data yang rentan terkena dampak spam bot serta menambahkan pesan error pada HTMLnya
Contoh:
// Illustrasi kode saja

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
// import lainnya

@Controller
@RequestMapping("/product")
public class ProductController {

    @PostMapping("/create")
    public String createProductPost(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        // cek apakah ada error validasi
        
        if (bindingResult.hasErrors()) {
            return "CreateProduct"; // Tetap di halaman form jika ada error
        }
        service.create(product);
        return "redirect:list";
    }
    @PostMapping("/edit")
    public String editProductPost(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "EditProduct"; // Tetap di halaman edit jika ada error
        }
        service.edit(product);
        return "redirect:list";
    }
}

package id.ac.ui.cs.advprog.eshop.model;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class Product {
    
    private String productId;
    @NotBlank(message = "Nama produk tidak boleh kosong!")
    private String productName;
    @Min(value = 1, message = "Jumlah minimal adalah 1")
    private int productQuantity;
}

# REFLECTION 2
1. Setelah melakukan unit test saya merasa semakin yakin bahwa kode saya tidak mengalami suatu kerusakan logika
   bahkan setelah saya lakukan berbagai macam refactoring dan merging menggunakan git merge, unit test tetap berjalan baik
   dan ini juga memudahkan saya dalam maintenance kode kedepannya karena dengan unit test sendiri saya bisa menambahkan fitur baru
   dan melakukan pengecekan hanya dengan tes yang sudah saya buat. Untuk jumlah unit test sendiri sebenarnya tidak berpengaruh karena
   tergantung pada koompleksitas program yang sedang dibuat yang jelas harus memiliki Positive case, Negative case, edge case sehingga
   semua jenis penggunaan user tercover. nah dengan 100% code coverage sendiri tidak menjamin kode tidak memiliki error karena kita bisa
   saja salah melakukan assertion ataupun kekurangan skenario.

2. Jika membuat functional test baru dengan duplikasi setup CreateProductFunctionalTest.java, maka akan menyebabkan kode jadi kotor
   pertama kita akan memiliki kode dengan setup yang sama persis pada dua file berbeda sehingga tidak efisien. kedua perawatan jadi semakin sulit karena
   jika misalkan ada 1 bagian yg perlu diubah pada setup maka kita harus mengubah di semua file dengan setup yang sama satu per satu. dan juga kode jadi
   semakin sulit dibaca karena konfigurasi yang berulang ulang malah dapat berujung mengalihkan fokus.
   Untuk perbaikan sendiri dapat menggunakan inheritance misalnya menggunakan satu base test case dan fokuskan semua konfigurasi ke base tersebut
   kemudian CreateProductFunctionalTest dan duplikatnya dapat extend ke base yang baru tersebut saja

# Module 2

# Reflection
1. Pada modul ini, awalnya saya menggunakan SonarQube sebagai alat untuk melakukan analisis kualitas kode karena fitur analisisnya cukup komprehensif dan banyak digunakan dalam praktik industri. Namun, dalam implementasinya SonarQube mengharuskan proses merging ke branch main agar analisis berjalan secara optimal, sedangkan ketentuan modul tidak mengizinkan perubahan langsung pada branch utama sebelum seluruh tahapan selesai. Kondisi tersebut membuat pendekatan ini kurang sesuai dengan kebutuhan tugas, sehingga saya memutuskan untuk beralih menggunakan PMD yang dapat dijalankan langsung melalui workflow CI tanpa perlu melakukan merge terlebih dahulu. Setelah PMD berhasil diintegrasikan ke dalam pipeline, ditemukan beberapa permasalahan code quality pada kelas EditDeleteFunctionalTest dan ProductRepositoryTest. Permasalahan utama yang terdeteksi adalah adanya duplikasi string literal yang digunakan berulang kali dalam beberapa method pengujian. Duplikasi tersebut melanggar prinsip clean code dan terdeteksi oleh aturan AvoidDuplicateLiterals pada PMD. Untuk mengatasi masalah ini, saya melakukan refactoring dengan mengganti string yang berulang menjadi satu variabel konstanta (private static final) sehingga tidak ada lagi literal yang ditulis berkali-kali. Perubahan ini membuat kode lebih terstruktur, lebih mudah dipelihara, dan sesuai dengan standar kualitas yang ditetapkan. Setelah refactoring dilakukan, hasil analisis PMD menunjukkan tidak ada lagi pelanggaran aturan, dan workflow CI dapat berjalan dengan sukses tanpa error.

2. Implementasi workflow yang saya buat telah memenuhi konsep Continuous Integration karena setiap push dan pull request secara otomatis memicu proses build serta menjalankan seluruh unit test yang ada di dalam proyek. Selain itu, workflow juga menjalankan analisis statis menggunakan PMD sehingga kualitas kode diperiksa secara otomatis setiap kali terjadi perubahan. Dengan pendekatan ini, setiap kontribusi kode langsung divalidasi tanpa perlu pengecekan manual, sehingga risiko integrasi kode yang bermasalah dapat diminimalkan. Proses CI tersebut memastikan bahwa kode yang digabungkan ke repository tetap konsisten, teruji, dan memenuhi standar kualitas yang telah ditetapkan. Hal ini sejalan dengan prinsip Continuous Integration yang menekankan integrasi kode secara berkelanjutan dan terotomatisasi. Untuk aspek Continuous Deployment, sistem telah dikonfigurasi agar secara otomatis melakukan deployment ke Render setiap kali terdapat push ke branch main. Mekanisme ini memungkinkan aplikasi langsung diperbarui tanpa intervensi manual setelah perubahan dikirim ke branch utama. Namun demikian, workflow CD saat ini belum memiliki dependensi langsung terhadap hasil pipeline CI, sehingga deployment tetap dapat berjalan meskipun ada kemungkinan kegagalan pada tahapan test atau analisis jika tidak dikontrol melalui branch protection. Oleh karena itu, meskipun implementasi ini sudah mengarah pada praktik Continuous Deployment, sistem masih dapat ditingkatkan dengan memastikan deployment hanya dilakukan setelah seluruh tahapan CI berhasil dan dengan menerapkan perlindungan branch untuk menjaga stabilitas versi produksi.



# Module 3

# Reflection

1. 
SRP
Pada fungsi ini, sayaa memisahkan CarController dari ProductController. Sebelumnya, CarController extends ProductController. Sekarang, masing-masing hanya bertanggung jawab pada satu domain model. Kita juga memecah semua service yang ada menjadi Service reader dan writer karena saya menyadari bahwa service tersebut fungsinya tercampur sehingga jika dipisah akan memenuhi ini. Dengan ini, setiap kelas memiliki satu fungsi saja sehingga prinsip ini terpenuhi

OCP
Pada prinsip ini saya menambahkan interface untuk setiap hal yang berhubungan dengan logic, seperti repository dan service. nantinya jika saya ingin menambah fitur Car yang datanya disimpan di Database , saya hanya butuh membuat class baru, seperti DatabaseCarRepositoryImpl, tanpa harus mengubah kode di CarController. Kode "terbuka" untuk dikembangkan, tapi "tertutup" untuk modifikasi kode inti.

LSP
Untuk ini salah satu contoh yang bisa saya berikan adalah menghapus CarController extends ProductController. Sebelumnya, CarController dipaksa menjadi subclass dari ProductController. Secara logika, CarController bukanlah suatu bagian dari ProductController dalam konteks web routing. Dengan memisahkan mereka, kita memastikan tidak ada perilaku superclass yang rusak saat digantikan oleh subclass.

ISP
Pada prinsip ini, contoh yang saya berikan adalah pemecahan service interface menjadi ReaderService dan WriterService dimana disini saya menyadari bahwa interface service yang saya miliki sekarang ternyata gabungan dari fungsi CRUD dan fungsi search, sehingga saya rasa akan lebih mudah jika saya memecahnya menjadi dua interface sehingga kode jadi lebih readable.

DIP
Pada prinsip ini, jika diamati pada kode saya, seperti service dan repository, mereka semua tidak ada yang memanggil kelas konkrit. Semuanya memanggil interface yang sudah disiapkan. Hal ini dikarenakan prinsip ini mengatakan bahwa high-level module (Controller) tidak lagi bergantung pada low-level module (Service Impl). Keduanya bergantung pada abstraksi (Interface). Sehingga dengan implementasi ini kode jadi jauh lebih aman dan readable karena kita bisa tinggal cek interface saja.

2. Pertama, kode jadi lebih mudah dirawat. dengan implementasi aturan solid, kode yang saya miliki jadi lebih terbaca. Misalkan, saya ingin memperbaiki fungsi update maka saya tidak perlu susah susah mencari fungsi itu karena dengan implementasi SOLID saya hanya perlu mengecek WriteService saja. Kemudian testing kode menjadi lebih mudah, dikarenakan saat melakukan testing kita tentu saja perlu menyesuaikan dengan dependensi yang ada. Karena SOLID sudah membersihkan hal terkait dependensi dengan misalnya menggunakan abstraksi, testing menjadi lebih mudah. Kode juga menjadi lebih fleksibel karena kita dapat mengatur perubahan pada low-level module tanpa menyentuh high-level module sama sekali seperti Controller.

3. Kode jadi sulit dibentuk karena pertama, pada dasarnya kode jadi semakin sulit dibaca. Misalnya, Kalau CarController masih extends ProductController, setiap kali kamu ubah URL mapping di Product, routing Car bisa ikut berantakan secara tidak sengaja. Kemudian kode juga jadi semakin sensitif, dikarenakan tanpa adanya abstraksi atau hal yang membatasi high-level module dan low-level module maka setiap saya mengganti kode bisa banyak sekali error yang terjadi di berbagai tempat lain. Kemudian, komponen juga menjadi saling mengunci, misalnya ProductController dengan CarController, jika dibiarkan saja relasi extends tersebut maka jika saya melakukan perubahan pada product controller kemungkinan besar car controller akan terpengaruh dan terjadi error lagi.

# MODULE 4
# Reflection
1. Mengacu dari Percival (2017), TDD ini sangat berguna karena memaksa saya untuk memikirkan berbagai skenario penggunaan dan edge cases sebelum mulai mengimplementasikan fitur. Dengan menetapkan tujuan pengujian di awal, kode yang dihasilkan menjadi lebih terarah dan minim bug karena sudah terpikirkan lebih dulu kasus-kasus yang menyebabkan bug. Namun, tantangan yang masih saya rasakan adalah keinginan untuk langsung menulis kode implementasi secara terburu-buru. Untuk pembuatan tes berikutnya, saya perlu lebih disiplin menahan diri dan benar-benar memastikan fase Red tuntas terlebih dahulu/ sudah menutup semua kasus sebelum melangkah ke fase Green agar siklus TDD benar-benar maksimal.

2. Secara keseluruhan, unit test yang telah saya buat di tutorial sudah berhasil menerapkan prinsip FIRST. Tes tersebut berjalan sangat cepat (Fast), berulang secara konsisten (Repeatable), dan Independent karena tidak bergantung pada database asli berkat penggunaan mocking dengan Mockito dan anotasi @BeforeEach. Selain itu, tes ini sudah bisa memvalidasi dirinya sendiri menggunakan berbagai asersi JUnit (seperti assertEquals dan assertThrows), serta cukup menyeluruh karena sudah menutup semua kasus, seperti happy path maupun negative cases seperti ID yang tidak valid. Untuk ke depannya, saya bisa meningkatkan kualitas tes ini dengan menambahkan skenario kasus yang lebih ekstrem, misalnya bagaimana jika objek order yang di-pass bernilai null atau daftar produknya kosong dan juga saya perlu meningkatkan kreativitas saya saat memikirkan alur program.

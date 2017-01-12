/**
 * Demonstrating the use of the HWIO API in combination with the iowarrior.
 * <h1>Welcome reader</h1>
 *
 * <p>
 * Happy to see you've come this far in studying these API's. Prepare yourselves
 * to learn about the <b>secret ingredient</b>.</p>
 *
 * <p>
 * This example is all about patterns applied. The iowarrior for instance is a
 * component in the lower layer of a layered architecture, providing a
 * connection between abstract IO required by an <u>application</u> and a
 * <u>device</u> that provides concrete or real IO, using a USB bus.<br>
 * Mating these components is very much like a marriage (pun intended): You have
 * a bride and groom, which you want to connect. Lets assume the warrior is of
 * the male gender, hence the groom. Warrior sounds masculine, doesn't it?.
 * Anyway, both have some benefit of the marriage: The application has a need
 * for the services the concrete device provides, the warrior provides the IO
 * for the application and gets to fiddle with it's bits. For such a marriage to
 * be successful, one, or both, must bring along something, to make the
 * matrimonial ceremony work. Since the iowarrior is the party that exists (at
 * least the class does) before the application comes along, the warrior is
 * quite single minded and only understands about BitOps and Bit-Factories. The
 * bride (the application) should bring her treasures, in this case such said
 * Bit-Factory. The ceremony then takes place inside the
 * <code>warrior.connect()</code> method, where the application bits are created
 * and connected to the warrior bits, by using the Factory's methods. The
 * warrior presents him selves in every invocation of a
 * <code>createXXXBit</code> method, so the bit can be created with a reference
 * to the warrior tucked inside.
 * </p>
 *
 * <p>
 * So you see, there is <b>no</b> secret ingredient, just the simple application
 * of the Abstract Factory Pattern, in which the application (or the
 * builder/creator of the application instance) provides the <u>concrete
 * factory</u>, the <u>client</u> is the iowarrior and the <u>concrete
 * products</u> are the bits, where the application is allowed to bake its own
 * versions, tweaked and ready to play their role in the application. In other
 * words: Things you knew all along, since studying <b>Design Patterns</b>.</p>
 * <hr>Greetings from <a href='http://www.imdb.com/title/tt0441773/quotes'>Mr.
 * Ping and step son Po</a>. They know there is no secret ingredient. They also
 * reminded me of Master Oogway's proverb:<br><i>
 * yesterday is history, tomorrow is a mystery, but today is a gift. That is why
 * it is called the present.</i>
 *
 */
package bitfactoryexample;

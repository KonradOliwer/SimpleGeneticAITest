/*
 * Copyright 2015 KonradOliwer.
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
package pwr.om.geneticai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import pwr.om.battlesystem.Store;
import pwr.om.battlesystem.actor.Actor;
import pwr.om.battlesystem.actor.BloodLine;
import pwr.om.battlesystem.actor.Hero;
import pwr.om.battlesystem.actor.Profession;

/**
 *
 * @author KonradOliwer
 */
public class Data {

    private static final File STORE_FOLDER = new File(Paths.get(System.getProperty("user.dir")).getParent().toString()
            + File.separator + "testActors");
    private static final File STORE_SAVED_COMPUTED_ACTORS_FOLDER
            = new File(Utils.generateFilePath(STORE_FOLDER.getAbsolutePath(), "computed_actors"));
    private static final File STORE_HEROES_FILE = new File(Utils.generateFilePath(STORE_FOLDER.getAbsolutePath(), "heroes.ser"));
    private final static int MAX_LVL = 200;
    private static Hero[] heroes;

    private static void generateTestData() {
        heroes = new Hero[MAX_LVL * Store.getInsctance().getBloodLines().length * Store.getInsctance().getProfessions().length];
        int index = 0;
        for (BloodLine bloodLine : Store.getInsctance().getBloodLines()) {
            for (Profession profession : Store.getInsctance().getProfessions()) {
                for (int lvl = 0; lvl < MAX_LVL; lvl++) {
                    heroes[index++] = new Hero(index, bloodLine, profession, lvl);
                }
            }
        }
        ObjectOutputStream oos = null;
        try {
            STORE_FOLDER.mkdir();
            oos = new ObjectOutputStream(new FileOutputStream(STORE_HEROES_FILE));
            oos.writeObject(heroes);
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (Exception ex) {
                Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void lasyLoadHeroes() {
        if (heroes == null) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(STORE_HEROES_FILE));
                heroes = (Hero[]) ois.readObject();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Data.class.getName()).log(Level.FINEST, null, ex);
                generateTestData();
                lasyLoadHeroes();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ois.close();
                } catch (Exception ex) {
                    Logger.getLogger(Data.class.getName()).log(Level.FINEST, null, ex);
                }
            }
        }
    }

    public static Actor[] getSavedActor() {
        lasyLoadHeroes();
        return heroes;
    }

    public static Actor getActor(BloodLine bloodLine, Profession profession, int lvl) {
        lasyLoadHeroes();
        int professionNumber = Store.getInsctance().getProfessions().length;
        return heroes[MAX_LVL + MAX_LVL * profession.getId() + MAX_LVL * professionNumber * bloodLine.getId()];
    }

    public static void saveActor(Actor actor, String name) {
        ObjectOutputStream oos = null;
        try {
            STORE_SAVED_COMPUTED_ACTORS_FOLDER.mkdir();
            oos = new ObjectOutputStream(new FileOutputStream(Utils.generateFilePath(STORE_SAVED_COMPUTED_ACTORS_FOLDER.getAbsolutePath(),
                    name)));
            oos.writeObject(actor);
        } catch (IOException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Actor loadActor(String name) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(Utils.generateFilePath(
                    STORE_SAVED_COMPUTED_ACTORS_FOLDER.getAbsolutePath(), name)));
            return (Actor) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static List<Actor> loadAllComputedActors() {
        ObjectInputStream ois = null;
        List<Actor> list = new ArrayList();
        for (File file : STORE_SAVED_COMPUTED_ACTORS_FOLDER.listFiles()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(file));
                list.add((Actor) ois.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ois.close();
                } catch (IOException ex) {
                    Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}

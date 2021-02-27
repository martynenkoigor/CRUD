package com.martynenkoigor.javacore;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

public class JavaIOSkillRepositoryImpl implements SkillRepository {

    final static private String filePath = "/Users/igor/projects/CRUD/skill.txt";

    JavaIOSkillRepositoryImpl() {
    }

    @Override
    public Skill create(String skillName) {
        File file = new File(filePath);
        BufferedWriter bw = null;
        BufferedReader br = null;
        Long lastId = 0L;
        Skill skill = new Skill(lastId, skillName);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            br = new BufferedReader(new FileReader(file));
            bw = new BufferedWriter(new FileWriter(file, true));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    lastId = Long.parseLong(parts[0].trim());
                }
            }
            bw.write((lastId + 1) + ";" + skillName + '\n');
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                br.close();
            } catch (Exception e) {
            }
        }
        return skill;
    }

    @Override
    public Skill read(Long skillId) {
        BufferedReader br = null;

        try {
            File file = new File(filePath);
            br = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                Long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                Skill skill = new Skill(id, name);

                if (id.equals(skillId)) {
                    return skill;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    @Override
    public Skill update(Skill newSkill) {
        List<Skill> skillList = getAll();
        BufferedWriter bw = null;

        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        try {
            bw = new BufferedWriter(new FileWriter(file));

            for (Skill oldSkill : skillList) {
                if (oldSkill.getId().equals(newSkill.getId())) {
                    oldSkill.setName(newSkill.getName());
                }
                bw.write(oldSkill.getId() + ";" + oldSkill.getName() + '\n');
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (Exception e) {
            }
        }

        return newSkill;
    }

    @Override
    public void deleteById(Long skillId) {
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            File inputFile = new File(filePath);
            File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
            br = new BufferedReader(new FileReader(inputFile));
            pw = new PrintWriter(new FileWriter(tempFile));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                Long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();

                if (!id.equals(skillId)) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Skill> getAll() {
        List<Skill> skillList = new LinkedList<>();
        BufferedReader br = null;

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return skillList;
            }
            br = new BufferedReader(new FileReader(file));

            String line = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                Long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                Skill skill = new Skill(id, name);

                if (!id.equals("") && !name.equals("")) {
                    skillList.add(skill);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
        return skillList;
    }

    public static void main(String[] args) {
        JavaIOSkillRepositoryImpl ss = new JavaIOSkillRepositoryImpl();

        ss.create("one");
        ss.create("two");
        Skill skill = new Skill(1, "new one");
        ss.update(skill);
        ss.create("three");
        ss.deleteById(2l);
        List<Skill> skillList = ss.getAll();
        for(Skill skill1 : skillList) {
            System.out.println(skill1);
        }
    }
}


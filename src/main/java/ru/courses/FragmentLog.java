package ru.courses;

enum FragmentLog {
    IP {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[0];
        }
    }, PROPERTIES {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[1] + parts[2];
        }
    }, DATA {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            String fragment = parts[3] + " " + parts[4];
            return fragment.substring(1, fragment.length() - 1);
        }
    }, REQUEST {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[5] + " " + parts[6] + " " + parts[7];
        }
    }, RESPONSE_CODE {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[8];
        }
    }, SIZE {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[9];
        }
    }, URL {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[10];
        }
    }, USER_AGENT {
        @Override
        public String getParameters(String str) {
            String fragment = "-";
            String[] parts = str.split(" ");
            if (parts.length > 12) {
                for (int i = 11; i < parts.length; i++)
                    fragment += parts[i] + " ";
                fragment = fragment.substring(1, fragment.length() - 1);
            }
            return fragment;
        }

    },
    USER_AGENT_BOT {
        @Override
        public String getParameters(String str) {
            String fragment = "-";
            String[] parts = str.split(" ");
            if (parts.length > 13) {
                fragment = parts[13];
                parts = fragment.split("/");
                fragment = parts[0];
            }
            return fragment;
        }

    };

    public abstract String getParameters(String str);

}
